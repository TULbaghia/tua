import React, {useState} from "react";
import {withNamespaces} from "react-i18next";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import "../css/UserInfo.css";
import {useLocale} from "./LoginContext";

import BreadCrumb from "./Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {api} from "../Api";
import {useHistory, useLocation} from "react-router";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "./Utils/Notification/NotificationProvider";
import queryString from "query-string";
import DataTable from "react-data-table-component";
import {useThemeColor} from "./Utils/ThemeColor/ThemeColorProvider";
import {ResponseErrorHandler} from "./Validation/ResponseErrorHandler";
import {useDialogPermanentChange} from "./Utils/CriticalOperations/CriticalOperationProvider";

const FilterComponent = ({filterText, onFilter, placeholderText}) => (
    <>
        <Form>
            <Form.Control type="text" value={filterText} onChange={onFilter} placeholder={placeholderText}/>
        </Form>
    </>
);

function UnassignManager(props) {
    const {t, i18n} = props;
    const history = useHistory();
    const {token, setToken} = useLocale();
    const [data, setData] = useState([
        {
            login: "",
            firstname: "",
            lastname: ""
        }
    ]);
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);
    const dispatchCriticalDialog = useDialogPermanentChange();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const [filterText, setFilterText] = React.useState('');
    const themeColor = useThemeColor()

    const filteredItems = data.filter(item => {
        return item.firstname && item.firstname.toLowerCase().includes(filterText.toLowerCase())
            || item.lastname && item.lastname.toLowerCase().includes(filterText.toLowerCase());
    });

    const columns = [
        {
            name: 'Login',
            selector: 'login',
            sortable: true,
        },
        {
            name: t('name'),
            selector: 'firstname',
            sortable: true,
        },
        {
            name: t('surname'),
            selector: 'lastname',
            sortable: true,
        },
        {
            name: t('deleteManager'),
            selector: 'delete',
            cell: row => {
                return (
                    <Button className="btn-sm" style={{backgroundColor: "#7749F8"}} onClick={event => {
                        handleUnassignManagerConfirmation(row.login, parseInt(parsedQuery.id));
                    }}>{t("delete")}</Button>
                )
            },
        },
    ];

    React.useEffect(() => {
        handleDataFetch();
    }, []);

    const getManagerData = async (login) => {
        const response = await api.getManagerData(login, {
            method: "GET",
            headers: {
                Authorization: token,
            }
        })
        return response;
    };

    const handleDataFetch = () => {
        if (token) {
            getManagersAssignedToHotel().then(r => {
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

    const getManagersAssignedToHotel = async () => {
        return await api.getManagersAssignedToHotel(parsedQuery.id, {headers: {Authorization: token}})
    }

    const handleUnassignManagerConfirmation = (login, setSubmitting) => (
        dispatchCriticalDialog({
            callbackOnSave: () => {
                handleUnassignManagerSubmit(login);
                history.push("/hotels")
            },
            callbackOnCancel: () => setSubmitting(false)
        })
    )

    const handleUnassignManagerSubmit = (login) => {
        getManagerData(login).then(res => {
            api.deleteManagerFromHotel(login, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: token,
                    "If-Match": res.headers.etag
                }
            }).then((res) => {
                dispatchNotificationSuccess({message: t('unassignManager.success')});
            }).catch(err => {
                ResponseErrorHandler(err, dispatchNotificationDanger);
            })
        })
    }

    const subHeaderComponentMemo = React.useMemo(() => {

        return <FilterComponent onFilter={e => {
            setFilterText(e.target.value);
        }} filterText={filterText} placeholderText={t('filterPhase')}/>;
    }, [filterText]);

    return (
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/">{t('adminDashboard')}</Link></li>
                <li className="breadcrumb-item"><Link to="/hotels">{t('hotelList')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('unassignManager')}</li>
            </BreadCrumb>
            <Container className="">
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div>
                            <h1 className="float-left">{t("unassignManager")}</h1>
                            <Button className="btn-secondary float-right m-2" onClick={event => {
                                getManagersAssignedToHotel().then(res => {
                                    setData(res.data);
                                    setFilterText('')
                                    dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
                                }).catch(err => {
                                    ResponseErrorHandler(err, dispatchNotificationDanger)
                                })
                            }}>{t("refresh")}</Button>
                        </div>
                        {data.length == 0 ?
                            <div className="float-left">{t("emptyListManagerUnassign")}</div>
                            : <DataTable className={"rounded-0"}
                                         noDataComponent={i18n.t('table.no.result')}
                                         columns={columns}
                                         data={filteredItems}
                                         subHeader
                                         noHeader={true}
                                         theme={themeColor === 'light' ? 'lightMode' : themeColor}
                                         subHeaderComponent={subHeaderComponentMemo}
                            />}
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(UnassignManager);
