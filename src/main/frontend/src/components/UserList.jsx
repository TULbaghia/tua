import { withNamespaces } from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import React from "react";
import DataTable from "react-data-table-component"
import {Button, Form, FormCheck} from "react-bootstrap";

function UserList(props) {
    const {t,i18n} = props

    const columns = [
        {
            name: 'Id',
            selector: 'id',
            sortable: true,
        },
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
            selector: 'name',
            sortable: true,
        },
        {
            name: t('surname'),
            selector: 'surname',
            sortable: true,
        },
        {
            name: "Odblokowane",
            selector: 'unlocked',
            cell: row => {
                const checked = row.unlocked;
                return(
                    <>
                        <Form.Check defaultChecked={checked} onChange={event => {
                            let value = event.target.checked;
                            // TODO
                        }}/>
                    </>
                )
            },
        },
        {
            name: "Aktywne",
            selector: 'active',
            cell: row => {
                let color = row.active ? "green" : "grey";
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
            name: "Edytuj",
            selector: 'edit',
            cell: row => {
              return(
                  <Button className="btn-sm">{t("edit")}</Button>
              )
            },
        },
        {
            name: "Zmień hasło",
            selector: 'changePassword',
            cell: row => {
                return(
                    <Button className="btn-sm">{t("change")}</Button>
                )
            },
        },
        {
            name: "Szczegóły",
            selector: 'details',
            cell: row => {
                return(
                    <Button className="btn-sm">{t("details")}</Button>
                )
            },
        },
    ];

    let data = [
        {
            id: 1,
            login: "logczak",
            email: "logczak@email.com",
            name: "Logan",
            surname: "Nak",
            unlocked: true,
            active: true,
        }
    ];

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
