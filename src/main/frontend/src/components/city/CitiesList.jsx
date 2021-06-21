import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import DataTable from "react-data-table-component"
import {Button, Form} from "react-bootstrap";
import {useLocale} from "../LoginContext";
import {api} from "../../Api"
import {useDialogPermanentChange, useDialog} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useHistory} from "react-router";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import { useThemeColor } from '../Utils/ThemeColor/ThemeColorProvider';
import {rolesConstant} from "../../Constants";



const FilterComponent = ({filterText, onFilter, placeholderText}) => (
    <>
        <Form>
            <Form.Control type="text" value={filterText} onChange={onFilter} placeholder={placeholderText}/>
        </Form>
    </>
);


function CitiesList(props) {
    const {t, i18n} = props
    const history = useHistory()
    const {token, setToken, currentRole, setCurrentRole} = useLocale();
    const [filterText, setFilterText] = React.useState('');
    const themeColor = useThemeColor()
    const [data, setData] = useState([
        {
            name: "",
        }
    ]);
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchCriticalDialog = useDialogPermanentChange();
    const dispatchCityDetailsDialog = useDialog();

    const filteredItems = data.filter(item => {
        return item.name && item.name.toLowerCase().includes(filterText.toLowerCase())
    });

    const columns = [
        {
            name: t('city'),
            selector: 'name',
            sortable: true,
            width: "10rem"
        },
    ];

    if(currentRole === rolesConstant.admin){
        columns.push({
            name: t('delete'),
            selector: 'delete',
            cell: row => {
                return(
                    <Button className="btn-sm" style={{backgroundColor: "#7749F8"}} onClick={async event => {
                        dispatchCriticalDialog({
                            callbackOnSave: () => deleteCity(row.id),
                        })
                    }}>{t('delete')}</Button>
                )
            }
        })

        columns.push({
                name: t('edit'),
                selector: 'edit',
                cell: row => {
                    return (
                        <Button className="btn-sm" style={{backgroundColor: "#7749F8"}} onClick={event => {
                            history.push('/cities/editCity?id=' + row.id);
                        }}>{t("edit")}</Button>
                    )
                },
        });
    }

    columns.push(
        {
            name: t('details'),
            selector: 'details',
            cell: row => {
                return(
                    <Button className="btn-sm" style={{backgroundColor: "#7749F8"}} onClick={event => {
                        dispatchCityDetailsDialog({
                            title: t('city.description'),
                            message: row.description,
                            textButtonSave: t('btn.close'),
                            noCancel: true
                        })
                    }}>{t('details')}</Button>
                )
            }
        },
    );

    function deleteCity(id){
        api.getCity(id, {method: 'GET',  headers: {Authorization: token}})
        .then(res => {
            console.log("etag")
            console.log(res)
            console.log(res.headers.etag)
            api.deleteCity(id, {headers: {
                Authorization: token,
                "If-Match": res.headers.etag
            }}).then(res => {
                dispatchNotificationSuccess({message: i18n.t('city.delete.success')})
            }).catch(err => {
                dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
            }).finally(() => fetchData());
        }).catch(err => dispatchNotificationDanger({message: i18n.t(err.response.data.message)}))
        .finally(() => fetchData());
    }


    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        if (token) {
            getAllCities().then(r => {
                console.log(r);
                setData(r.data);
            }).catch(r => {
                if (r.response != null) {
                    if (r.response.status === 403) {
                        history.push("/errors/forbidden")
                    } else if (r.response.status === 500) {
                        history.push("/errors/internal")
                    }
                }
                console.log(r)
            })
        }
    }

    const getAllCities = async () => {
        return await api.getAllCities({headers: {Authorization: token}});
    }

    const subHeaderComponentMemo = React.useMemo(() => {

        return <FilterComponent onFilter={e => {
            setFilterText(e.target.value);
        }} filterText={filterText} placeholderText={t('filterPhase')}/>;
    }, [filterText]);

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/">{t('adminDashboard')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('cities.list.bread_crumb')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <div>
                    <h1 className="float-left">{t('cities.list.header')}</h1>
                    <Button className="btn-secondary float-right m-2" onClick={event => {
                        getAllCities().then(res => {
                            setData(res.data);
                            setFilterText('')
                            dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
                        }).catch(err => {
                            ResponseErrorHandler(err, dispatchNotificationDanger)
                        })
                    }}>{t("refresh")}</Button>
                    {token !== null && token !== '' && currentRole === rolesConstant.admin ? (
                        <Button className="btn-primary float-right m-2" style={{backgroundColor: "#7749F8"}} onClick={event => {
                            history.push('/cities/add');
                        }}>{t('addCity.action')}</Button>
                    ) : ( null )}
                </div>
                <DataTable className={"rounded-0"}
                    noDataComponent={i18n.t('table.no.result')}
                    columns={columns}
                    data={filteredItems}
                    subHeader
                    theme={themeColor}
                    subHeaderComponent={subHeaderComponentMemo}
                />
            </div>
        </div>
    )
}
export default withNamespaces()(CitiesList);
