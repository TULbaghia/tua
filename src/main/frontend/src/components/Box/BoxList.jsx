import React, {Component, useEffect, useState} from "react";
import {useLocale} from "../LoginContext";
import BoxItem from "./BoxItem";
import "./BoxListStyle.scss"
import {withNamespaces} from "react-i18next";
import {Button, Form, Row} from "react-bootstrap";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {rolesConstant} from "../../Constants";
import i18n from "i18next";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useHistory, useLocation} from "react-router";
import queryString from "query-string";

function BoxList(props) {

    const [boxes, setBoxes] = useState([])
    const {token, username, currentRole} = useLocale();
    const history = useHistory();
    const location = useLocation();

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();

    const [searchTerm, setSearchTerm] = React.useState('');
    const hotelIdFromUrl = queryString.parse(location.search).id;

    const decideFetch = (refresh = false) => {
        if (hotelIdFromUrl !== undefined && (currentRole === rolesConstant.manager || currentRole === rolesConstant.client)) {
            fetchHotelDataById(refresh);
        } else if (hotelIdFromUrl === undefined && currentRole === rolesConstant.manager) {
            fetchHotelData(refresh);
        }
    }

    const handleSearchBox = (event) => {
        setSearchTerm(event.target.value);
        if (event.target.value !== '') {
            setBoxes(filteredItems);
        } else {
            decideFetch();
        }
    }

    const filteredItems = boxes.filter(item => {
        return item.description && item.description.toLowerCase().includes(searchTerm.toLowerCase());
    });

    useEffect(() => {
        decideFetch();
    }, [token]);

    const fetchHotelData = (refresh = false) => {
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: token,
            },
        };

        fetch("/resources/boxes/all/" + username, requestOptions)
            .then((res) => res.json())
            .then((boxes) => {
                setBoxes(boxes);
            })
            .catch(err => {
                ResponseErrorHandler(err, dispatchNotificationDanger)
            });

        if (refresh) {
            dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
        }
    }

    const fetchHotelDataById = (refresh = false) => {
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: token,
            },
        };

        fetch("/resources/boxes/all/id/" + hotelIdFromUrl, requestOptions)
            .then((res) => res.json())
            .then((boxes) => {
                setBoxes(boxes);
            })
            .catch(err => {
                ResponseErrorHandler(err, dispatchNotificationDanger)
            });
        if (refresh) {
            dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
        }
    }

    const handleIsManager = () => {
        return currentRole === rolesConstant.manager;
    }

    const handleModify = (boxId) => {
        history.push("/boxes/modify?id=" + boxId)
    };

    return (
        <div id={"box-list"} className={"container-fluid"}>

            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('mainPage')}</Link></li>
                {currentRole === rolesConstant.manager && (
                    <li className="breadcrumb-item"><Link to="/">{i18n.t('managerDashboard')}</Link></li>
                )}
                {currentRole === rolesConstant.client && (
                    <li className="breadcrumb-item"><Link to="/">{i18n.t('userDashboard')}</Link></li>
                )}
                {hotelIdFromUrl !== undefined && (
                    <li className="breadcrumb-item"><Link to="/hotels">{i18n.t('hotelInfo')}</Link></li>
                )}
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('boxList.navbar.title')}</li>
            </BreadCrumb>
            <div className={"row"}>
                <div className={"box-grid container mb-5"}>

                    <div className={"row"}>
                        <h1 className="col-md-6">{i18n.t('boxList.navbar.title')}</h1>
                        <div className={"col-lg-6 col-12  d-flex flex-wrap flex-sm-nowrap justify-content-around justify-content-sm-start flex-row-reverse align-content-center"}>
                            <Button className="btn-secondary float-right m-2" onClick={event => {
                                decideFetch(true);
                                setSearchTerm('');
                            }}>
                                {i18n.t("refresh")}
                            </Button>
                            {token !== null && token !== '' && currentRole === rolesConstant.manager ? (
                                <Button className="btn-primary float-right m-2" onClick={event => {
                                    history.push('/boxes/add');
                                }}>{i18n.t("addBox")}</Button>
                            ) : (<></>)}
                            <input
                                className="input float-right m-2 w-100"
                                type="text"
                                placeholder={i18n.t("search.box")}
                                value={searchTerm}
                                onKeyUp={handleSearchBox}
                                onChange={handleSearchBox}
                            />
                        </div>
                    </div>

                    <div style={{
                        display: 'flex',
                        flex: '1',
                        flexDirection: 'row',
                    }}>
                        <div className='row-wrapper w-100' style={{padding: '1rem'}}>
                            <div className={"row"} style={{display: "flex"}}>
                                {boxes.length === 0 ? (<div>{i18n.t('table.no.result')}</div>) : (
                                    <>
                                        {boxes.map((box) => (
                                            <div style={{display: "flex"}} className={"col-sm-6 col-md-3 my-2"}>
                                                <BoxItem
                                                    key={box.id}
                                                    onModify={handleModify}
                                                    box={box}
                                                    isManager={handleIsManager}
                                                />
                                            </div>
                                        ))}
                                    </>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default withNamespaces()(BoxList);