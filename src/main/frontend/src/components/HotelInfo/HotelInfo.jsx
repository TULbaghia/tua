import React, {useState} from "react";
import cat from "../../images/cat.png"
import {withNamespaces} from "react-i18next";
import queryString from "query-string";
import "../../css/floatingbox.css";
import {useHistory, useLocation} from "react-router";
import {ListGroup, Tab, Tabs} from "react-bootstrap";
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


function Home(props) {
    const {t, i18n} = props
    const location = useLocation();
    const {currentRole} = useLocale();
    const history = useHistory();
    const parsedQuery = queryString.parse(location.search);
    const [hotelData, setHotelData] = useState({
        address: "",
        cityName: "",
        description: "",
        name: "",
        rating: "",
        image: "",
    });

    const [ratingData, setRatingData] = useState([
        {
            id: "",
            comment: "",
            createdBy: "",
            hidden: "",
            rate: "",
            creationDate: ""
        }
    ]);

    React.useEffect(() => {
        handleHotelDataFetch();
        handleRatingDataFetch();
        sortRatingData();
    }, []);

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

    const getHotelInfo = async () => {
        return await api.getHotel(parsedQuery.id);
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
            history.push("/boxes?id=" + parsedQuery.id)
        }
    }

    const getRatingInfo = async () => {
        return await api.getAllRatings(parsedQuery.id);
    }

    const sortRatingData = () => {
        ratingData.sort((a, b) => a.id - b.id);
    }

    return (
        <>
            <div className="container">
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
            </div>
            <div id={"hotelInfo"} className={"container mt-2 p-4 mb-5"}>
                <div className={"row"}>
                    <div className={"col-md-6 col-sm-8 col-10 mb-2"}>
                        <h2>{hotelData.name}</h2>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-4 col-sm-6 col-8 mb-3"}>
                        <img alt="cat" className="img-fluid"
                             src={hotelData.image === undefined ? cat : hotelData.image}/>
                    </div>
                    <div className={"col-md-8 col-sm-6 col-4"}>
                        <Tabs defaultActiveKey="description" transition={false} id="tab" onSelect={handleBoxListClick}>
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
                            {(currentRole === rolesConstant.manager || currentRole === rolesConstant.client) && (
                                <Tab eventKey="boxlist" title={t('boxList.navbar.title')}>
                                    <div className={"text-justify mt-2"}>
                                        {hotelData.description}
                                    </div>
                                </Tab>
                            )}
                        </Tabs>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-4 d-flex align-items-center"}>
                        <h4 className={"m-0"}>{t('comments')}</h4>
                    </div>
                    <div className={"col-md-8 d-flex align-items-center justify-content-end"}>
                        <span className={"mr-3"}>{t('averageRating')}</span>
                        <Rating
                            size={"large"}
                            defaultValue={0}
                            value={hotelData.rating}
                            readOnly
                            precision={0.5}
                        />
                        <span className={"ml-3"}>[{hotelData.rating}]</span>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-12 mt-3"}>
                        {ratingData.length > 0 && ratingData.map((item) => (
                            <RatingComponent id={item.id} rate={item.rate} login={item.createdBy} content={item.comment}
                                             hidden={item.hidden} date={dateConverter(item.creationDate.slice(0, -5))}/>
                        ))}
                    </div>
                </div>
            </div>
        </>
    );
}

export default withNamespaces()(Home);