import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import DataTable from "react-data-table-component"
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {useLocale} from "../LoginContext";
import {api} from "../../Api"
import {useDialogPermanentChange, useDialog} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useHistory} from "react-router";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {useThemeColor} from '../Utils/ThemeColor/ThemeColorProvider';
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

    if (currentRole === rolesConstant.admin) {

        columns.push({
            name: t('edit'),
            selector: 'edit',
            cell: row => {
                return (
                    <Button className="btn-sm" variant="purple" onClick={event => {
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
                return (
                    <Button className="btn-sm" variant="purple" onClick={event => {
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
        <div className={"container-fluid"}>
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/">{t('adminDashboard')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('cities.list.bread_crumb')}</li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={9} lg={8} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div>
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
                                    <Button className="btn-secondary float-right m-2"
                                            onClick={event => {
                                                history.push('/cities/add');
                                            }}>{t('addCity.action')}</Button>
                                ) : (null)}
                            </div>
                            <DataTable className={"rounded-0"}
                                       noDataComponent={i18n.t('table.no.result')}
                                       columns={columns}
                                       data={filteredItems}
                                       subHeader
                                       noHeader={true}
                                       theme={themeColor === 'light' ? 'lightMode' : themeColor}
                                       subHeaderComponent={subHeaderComponentMemo}
                            />
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}


export default withNamespaces()(CitiesList);
