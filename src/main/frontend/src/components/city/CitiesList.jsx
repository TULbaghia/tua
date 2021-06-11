import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import DataTable from "react-data-table-component"
import {Button, Form} from "react-bootstrap";
import {useLocale} from "./../LoginContext";
import {api} from "../../Api"
import {useDialogPermanentChange} from "./../Utils/CriticalOperations/CriticalOperationProvider";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "./../Utils/Notification/NotificationProvider";
import {useHistory} from "react-router";
import {ResponseErrorHandler} from "./../Validation/ResponseErrorHandler";
import { useThemeColor } from './../Utils/ThemeColor/ThemeColorProvider';

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
    const {token, setToken} = useLocale();
    const [filterText, setFilterText] = React.useState('');
    const themeColor = useThemeColor()
    const [data, setData] = useState([
        {
            name: "",
        }
    ]);
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const filteredItems = data.filter(item => {
        return item.name && item.name.toLowerCase().includes(filterText.toLowerCase())
    });

    const columns = [
        {
            name: 'Name',
            selector: 'name',
            sortable: true,
            width: "10rem"
        },
        {
            name: t('edit'),
            selector: 'edit',
            cell: row => {
                return (
                    <Button className="btn-sm" onClick={event => {
                        // todo 
                        // history.push('/editOtherAccount?login=' + row.login);
                    }}>{t("edit")}</Button>
                )
            },
        },
        {
            name: t('details'),
            selector: 'details',
            cell: row => {
                return(
                    <Button className="btn-sm" onClick={event => {
                        // todo
                        // history.push('/accounts/userInfo?login=' + row.login);
                    }}>{t('details')}</Button>
                )
            }
        },
    ];

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
            });
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
