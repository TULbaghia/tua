import React, {useState} from "react";
import {withNamespaces} from "react-i18next";
import {Button, Container, Form} from "react-bootstrap";
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

function AssignManager(props) {
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
            width: "15rem"
        },
        {
            name: t('name'),
            selector: 'firstname',
            sortable: true,
            width: "15rem"
        },
        {
            name: t('surname'),
            selector: 'lastname',
            sortable: true,
            width: "15rem"
        },
        {
            name: t('assign'),
            selector: 'assign',
            cell: row => {
                return (
                    <Button className="btn-sm" onClick={event => {
                        handleAssignManagerConfirmation(row.login, parseInt(parsedQuery.id));
                    }}>{t("assign")}</Button>
                )
            },
        },
    ];

    React.useEffect(() => {
        handleDataFetch();
    }, []);

    const getManagerData = async (login) => {
        const response = await api.getManagerData(login,{
            method: "GET",
            headers: {
                Authorization: token,
            }})
        return response;
    };

    const handleDataFetch = () => {
        if (token) {
            getAllManagers().then(r => {
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

    const getAllManagers = async () => {
        return await api.getAllManagersList({headers: {Authorization: token}})
    }

    const handleAssignManagerConfirmation = (login, setSubmitting) => (
        dispatchCriticalDialog({
            callbackOnSave: () => {handleAssignManagerSubmit(login); history.push("/hotels")},
            callbackOnCancel: () => setSubmitting(false)
        })
    )

    const handleAssignManagerSubmit = (login) => {
        getManagerData(login).then(res => {
            api.addManagerToHotel(parseInt(parsedQuery.id), login, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: token,
                    "If-Match": res.headers.etag
                }
            }).then((res) => {
                dispatchNotificationSuccess({message: t('assignManager.success')});
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
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/">{t('adminDashboard')}</Link></li>
                <li className="breadcrumb-item"><Link to="/hotels">{t('hotelList')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('assignManager')}</li>
            </BreadCrumb>
            <Container className="main-wrapper floating-box">
                <div>
                    <h1 className="float-left">{t("assignTitle")}</h1>
                    <Button className="btn-secondary float-right m-2" onClick={event => {
                        getAllManagers().then(res => {
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
            </Container>
        </div>
    );
}

export default withNamespaces()(AssignManager);
