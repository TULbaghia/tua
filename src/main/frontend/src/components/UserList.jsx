import { withNamespaces } from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import DataTable from "react-data-table-component"
import {Button, Form, FormCheck} from "react-bootstrap";
import {getToken} from "../store/authentication";
import {api} from "../Api";
import {useDialogPermanentChange} from "./CriticalOperations/CriticalOperationProvider";
import {useNotificationDangerAndLong, useNotificationSuccessAndShort} from "./Notification/NotificationProvider";

function UserList(props) {
    const {t,i18n} = props
    const [data, setData] = useState([
        {
            login: "",
            email: "",
            name: "",
            surname: "",
            unlocked: false,
            active: false,
        }
    ]);
    const dispatchDialog = useDialogPermanentChange();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndLong();

    const columns = [
        {
            name: 'Login',
            selector: 'login',
            sortable: true,
        },
        {
            name: 'E-mail',
            selector: 'email',
            sortable: true,
            minWidth: 100
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
            name: t('unlocked'),
            selector: 'enabled',
            cell: row => {
                const checked = row.enabled;
                return(
                    <>
                        <Form.Check defaultChecked={checked} onChange={event => {
                            let value = event.target.checked;
                            dispatchDialog({
                                callbackOnSave: () => {value ? unblockAccount(row.login) : blockAccount(row.login)},
                                callbackOnCancel: () => {
                                    console.log("Cancel")
                                    event.target.checked = !value;
                                },
                            })
                        }}/>
                    </>
                )
            },
        },
        {
            name: t('active'),
            selector: 'confirmed',
            cell: row => {
                let color = row.confirmed ? "green" : "grey";
                return (
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill={color}
                         className="bi bi-check" viewBox="0 0 16 16">
                        <path
                            d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425a.267.267 0 0 1 .02-.022z"/>
                    </svg>
                )
            },
        },
        {
            name: t('edit'),
            selector: 'edit',
            cell: row => {
              return(
                  <Button className="btn-sm">{t("edit")}</Button>
              )
            },
        },
        {
            name: t('changePassword'),
            selector: 'changePassword',
            cell: row => {
                return(
                    <Button className="btn-sm">{t("change")}</Button>
                )
            },
        },
        {
            name: t('details'),
            selector: 'details',
            cell: row => {
                return(
                    <Button className="btn-sm">{t("details")}</Button>
                )
            },
        },
    ];

    useEffect(() => {
        if(getToken()) {
            getAllAccounts().then(r => {
                console.log(r);
                setData(r.data);
            }).catch(r => {
                console.log(r)
            });
        }
    }, []);

    const getAllAccounts = async () => {
        return await api.getAllAccountsList({headers: {Authorization: "Bearer " + getToken().data}})
    }

    const blockAccount = (login) => {
        api.blockAccount(login, {headers: {Authorization: "Bearer " + getToken().data}}).then(res => {
            dispatchNotificationSuccess({message: i18n.t('accountBlockSuccess')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        })
    }

    const unblockAccount = (login) => {
        api.unblockAccount(login, {headers: {Authorization: "Bearer " + getToken().data}}).then(res => {
            dispatchNotificationSuccess({message: i18n.t('accountUnblockSuccess')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        })
    }

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('userList')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <div>
                    <h1 className="float-left">{t('userList')}</h1>
                    <Button className="btn-secondary float-right m-2">{t("refresh")}</Button>
                    <Button className="btn-primary float-right m-2">{t("add")}</Button>
                </div>
                <DataTable
                    columns={columns}
                    data={data}
                />
            </div>
        </div>
    )
}
export default withNamespaces()(UserList);
