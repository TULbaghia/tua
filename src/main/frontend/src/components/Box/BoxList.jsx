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
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useHistory} from "react-router";

const FilterComponent = ({filterText, onFilter, placeholderText}) => (
    <>
        <Form>
            <Form.Control type="text" value={filterText} onChange={onFilter} placeholder={placeholderText}/>
        </Form>
    </>
);

function BoxList(props) {

    const [boxes, setBoxes] = useState([])
    const {token, username, currentRole} = useLocale();
    const history = useHistory();

    const dispatchDialog = useDialogPermanentChange();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();

    const [searchTerm, setSearchTerm] = React.useState('');
    const [filterText, setFilterText] = React.useState('');

    const handleSearchBox = (event) => {
        // debugger;
        setSearchTerm(event.target.value);
        // setBoxes(boxes.filter((b) => b.id !== event.target.value))
        setBoxes(filteredItems);


        // if(event.target.value !== '') {
        //     filteredItems(event.target.value)
        // }
    }

    const subHeaderComponentMemo = React.useMemo(() => {

        return <FilterComponent onFilter={e => {
            setFilterText(e.target.value);
        }} filterText={filterText} placeholderText={i18n.t('filterPhase')}/>;
    }, [filterText]);

    const filteredItems = boxes.filter(item => {
        return item.description && item.description.toLowerCase().includes(filterText.toLowerCase());
    });
    //
    // const handleSearchTermChange = (event) => {
    //     setSearchTerm(event.target.value)
    //     if(event.target.value !== '') {
    //         filteredItems(event.target.value)
    //     }
    // }

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: token,
            },
        };

        fetch("/resources/boxes/all/" + username, requestOptions)
            .then((res) => res.json())
            .then(
                (boxes) => {
                    setBoxes(boxes);
                    // setFilterText('')
                    dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
                },
                (error) => {
                    console.log(error);
                }
            ).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger)
        });
    }

    const handleModify = (userId) => {
        props.history.push({
            pathname: "/",
            state: {idOfBox: userId},
        });
    };

    const handleDelete = (boxId) => {
        setBoxes(boxes.filter((b) => b.id !== boxId))
    };

    return (
        <div id={"box-list"} className={"container"}>

            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('mainPage')}</Link></li>
                {currentRole === rolesConstant.manager && (
                    <li className="breadcrumb-item"><Link to="/">{i18n.t('managerDashboard')}</Link></li>
                )}
                {currentRole === rolesConstant.client && (
                    <li className="breadcrumb-item"><Link to="/">{i18n.t('userDashboard')}</Link></li>
                )}
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('boxList.navbar.title')}</li>
            </BreadCrumb>

            {/*<div className="floating-box">*/}
            <div style={{position: "absolute"}} className={"box-grid"}>

                <div className={"row"}>
                    <h1 className="col-md-6">{i18n.t('boxList.navbar.title')}</h1>
                    <div className={"col-md-6"}>
                    <Button className="btn-secondary float-right m-2" onClick={event => {
                        fetchData()
                    }}>
                        {i18n.t("refresh")}
                    </Button>
                    {token !== null && token !== '' && currentRole === rolesConstant.manager ? (
                        <Button className="btn-primary float-right m-2" onClick={event => {
                            history.push('/hotels/addHotel');
                        }}>{i18n.t("addHotel")}</Button>
                    ) : (null)}
                    <input
                        className="input float-right m-2"
                        type="text"
                        placeholder={i18n.t("search.hotel")}
                        value={searchTerm}
                        onKeyUp={handleSearchBox}
                        onChange={handleSearchBox}
                    />
                    </div>
                </div>


                <div style={{
                    maxHeight: '35rem',
                    display: 'flex',
                    flex: '1',
                    flexDirection: 'row',
                    // width: '75rem',
                    overflowY: 'scroll'
                }}>
                    <div className='row-wrapper' style={{padding: '1rem'}}>
                        <div className={"row"} style={{display: "flex"}}>
                            {boxes.map((box) => (
                                <div style={{display: "flex"}} className={"col-sm-6 col-md-3 my-2"}>
                                    <BoxItem
                                        key={box.id}
                                        onDelete={handleDelete}
                                        onModify={handleModify}
                                        box={box}
                                    />
                                </div>
                            ))}
                        </div>
                        {/*</div>*/}
                    </div>


                </div>
            </div>
        </div>
    );
}

export default withNamespaces()(BoxList);