import React, {useState} from "react";
import cat from "../../images/cat.png"
import {withNamespaces} from "react-i18next";
import queryString from "query-string";
import "../../css/HotelInfo.css";
import {useHistory, useLocation} from "react-router";
import {Button, ListGroup, Tab, Tabs} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import RatingComponent from "./RatingComponent";
import {faCity, faMapMarkedAlt} from '@fortawesome/free-solid-svg-icons'
import {Rating} from "@material-ui/lab";
import {api} from "../../Api";
import {dateConverter} from "../../i18n";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {rolesConstant} from "../../Constants";
import {useLocale} from "../LoginContext";
import {v4} from "uuid";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {useNotificationDangerAndInfinity, useNotificationSuccessAndShort} from "../Utils/Notification/NotificationProvider";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import NewRatingComponent from "./NewRatingComponent";
import axios from "axios";


function HotelInfo(props) {
    const {t, i18n} = props
    const location = useLocation();
    const {token, setToken, currentRole} = useLocale();
    const history = useHistory();
    const parsedQuery = queryString.parse(location.search);
    const dispatchDialog = useDialogPermanentChange();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const [userBookings, setUserBookings] = useState([])
    const [activeKey, setActiveKey] = useState("description")
    const [hotelData, setHotelData] = useState({
        address: "",
        cityName: "",
        description: "",
        name: "",
        rating: "",
        image: "",
    });

    const [ratingData, setRatingData] = useState([]);
    const [hotelEtag, setHotelEtag] = useState();

    React.useEffect(() => {
        refreshData()
    }, []);

    const refreshData = () => {
        setRatingData([]);
        setUserBookings([]);
        setHotelData({
            address: "",
            cityName: "",
            description: "",
            name: "",
            rating: "",
            image: "",
        });

        handleHotelDataFetch();
        handleRatingDataFetch();
        handleUsersBookingsDataFetch();
        sortRatingData();
    }

    const handleHotelDataFetch = () => {
        getHotelInfo().then(res => {
            console.log(res.data);
            setHotelData(res.data);
        }).catch(err => {
            if (err.response != null) {
                if (err.response.status === 500) {
                    history.push("/errors/internal");
                }
            }
        });
    }

    const handleUsersBookingsDataFetch = () => {
        console.log("boookingsFetch")
        if (currentRole === rolesConstant.client) {
            getUsersBookings().then(res => {
                console.log(res.data);
                setUserBookings(res.data.filter(x => x.bookingStatus === 'FINISHED'));
            }).catch(err => {
                if (err.response != null) {
                    if (err.response.status === 500) {
                        history.push("/errors/internal");
                    }
                }
            })
        }
    }

    const getUsersBookings = async () => {
        let id = parsedQuery.id;
        return await axios.get(`${process.env.REACT_APP_API_BASE_URL}/resources/bookings/ended/` + id, {
            headers: {
                Authorization: token,
            }
        })
    }

    const getHotelInfo = async () => {
        if (currentRole === rolesConstant.admin) {
            return await getHotelData(parsedQuery.id)
        } else {
            return await api.getHotel(parsedQuery.id);
        }
    }

    const handleRatingDataFetch = () => {
        getRatingInfo().then(res => {
            console.log(res.data);
            setRatingData([
                ...res.data,
            ]);
        }).catch(err => {
            if (err.response != null) {
                if (err.response.status === 500) {
                    history.push("/errors/internal");
                }
            }
        });
    }

    const handleBoxListClick = (key) => {
        if (key === "boxlist") {
            history.push("/boxes/" + parsedQuery.id)
        } else if (key === "refresh") {
            refreshData();
            document.querySelector("#tab-tab-refresh").blur();
            setActiveKey("description")
        } else if (key === "delete") {
            handleDeleteHotel();
        } else {
            setActiveKey(key)
        }
    }

    const getRatingInfo = async () => {
        return await api.getAllRatingsList(parsedQuery.id);
    }

    const sortRatingData = () => {
        ratingData.sort((a, b) => a.id - b.id);
    }

    const getHotelData = async (id) => {
        const response = await api.getOtherHotelInfo(id, {
            method: "GET",
            headers: {
                Authorization: token,
            }
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
            history.push("/hotels")
        });
        setHotelEtag(response.headers.etag);
        return response;
    };

    const deleteHotel = () => {
        api.deleteHotel(parsedQuery.id, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": hotelEtag
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: i18n.t('hotelDelete.success')});
            history.push("/hotels");
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
        });
    }

    const handleDeleteHotel = () => {
        dispatchDialog({
            callbackOnSave: () => {
                deleteHotel()
            },
        })
    }

    return (
        <div className={"container-fluid"}>
                <BreadCrumb>
                    <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                    {currentRole === rolesConstant.admin && (
                        <li className="breadcrumb-item"><Link to="/">{t('adminDashboard')}</Link></li>
                    )}
                    {currentRole === rolesConstant.manager && (
                        <li className="breadcrumb-item"><Link to="/">{t('managerDashboard')}</Link></li>
                    )}
                    {currentRole === rolesConstant.client && (
                        <li className="breadcrumb-item"><Link to="/">{t('userDashboard')}</Link></li>
                    )}
                    <li className="breadcrumb-item"><Link to="/hotels">{t('hotelList')}</Link></li>
                    <li className="breadcrumb-item active" aria-current="page">{t('hotelInfo')}</li>
                </BreadCrumb>
            <div id={"hotelInfo"} className={"container mt-2 p-4 mb-2"}>
                <div className={"row"}>
                    <div className={"col-md-6 col-sm-8 col-10 mb-2"}>
                        <h2>{hotelData.name}</h2>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-4 col-sm-12 col-12 col-xs-12 mb-3"}>
                        <img alt="cat" className="img-fluid"
                             src={hotelData.image === undefined ? cat : hotelData.image}/>
                    </div>
                    <div className={"col-md-8 col-sm-12 col-12 col-xs-12"}>
                        <Tabs defaultActiveKey="description" transition={false} id="tab" activeKey={activeKey}
                              onSelect={handleBoxListClick}>
                            <Tab eventKey="description" title={t('descriptionHotel')}>
                                <div className={"text-justify mt-2"}>
                                    {hotelData.description}
                                </div>
                            </Tab>
                            <Tab eventKey="location" title={t('location')}>
                                <ListGroup variant={"flush"}>
                                    <ListGroup.Item className={"d-flex align-items-center"}>
                                        <FontAwesomeIcon icon={faCity} size={"2x"}/>
                                        <span className={"ml-3"}>{hotelData.cityName}</span>
                                    </ListGroup.Item>
                                    <ListGroup.Item className={"d-flex align-items-center"}>
                                        <FontAwesomeIcon icon={faMapMarkedAlt} size={"2x"}/>
                                        <span className={"ml-3"}>{hotelData.address}</span>
                                    </ListGroup.Item>
                                </ListGroup>
                            </Tab>
                            <Tab eventKey="refresh" tabClassName={"ml-auto"} title={t('refresh')}/>
                            <Tab eventKey="boxlist" tabClassName={"ml-0"} title={t('boxList.navbar.title')}/>
                            {currentRole === rolesConstant.admin && (
                                <Tab eventKey="delete" title={t('delete')}/>
                            )}
                        </Tabs>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-12 py-2 d-block d-md-none"}>
                        <hr/>
                    </div>
                    <div className={"col-md-4 col-12 d-flex align-items-center"}>
                        <h4 className={"m-0"}>{t('comments')}</h4>
                    </div>
                    <div
                        className={"col-md-8 col-12 d-flex mt-2 mt-md-0 align-items-center justify-content-start justify-content-md-end"}>
                        <span className={"mr-3"}>{t('averageRating')}</span>
                        <Rating
                            size={"large"}
                            defaultValue={0}
                            value={hotelData.rating}
                            readOnly
                            precision={0.1}
                        />
                        <span
                            className={"ml-3"}>[{hotelData.rating !== undefined ? hotelData.rating : t("ratings.noRatings")}]</span>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-12 mt-3"}>
                        {ratingData.length > 0 && ratingData.map((item) => (
                            <RatingComponent key={v4()} triggerRefresh={refreshData} id={item.id} rate={item.rate}
                                             login={item.createdBy} content={item.comment}
                                             hidden={item.hidden} date={dateConverter(item.creationDate.slice(0, -5))}
                                             modificationDate={item.modificationDate}/>
                        ))}
                        {(currentRole === rolesConstant.client && userBookings.length > 0) &&
                        <NewRatingComponent triggerRefresh={refreshData} placeholder={t('add.new.comment')}
                                            header={t('add.new.rating')}
                                            buttonText={t('add.rating')} bookings={userBookings}/>
                        }
                    </div>
                </div>
            </div>
        </div>
    );
}

export default withNamespaces()(HotelInfo);