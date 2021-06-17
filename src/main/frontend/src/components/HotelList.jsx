import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./Partial/BreadCrumb";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import DataTable from "react-data-table-component"
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import {useLocale} from "./LoginContext";
import {api} from "../Api";
import {useDialogPermanentChange} from "./Utils/CriticalOperations/CriticalOperationProvider";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "./Utils/Notification/NotificationProvider";
import {useHistory, useLocation} from "react-router";
import {ResponseErrorHandler} from "./Validation/ResponseErrorHandler";
import {useThemeColor} from './Utils/ThemeColor/ThemeColorProvider';
import {rolesConstant} from "../Constants";
import axios from "axios";
import hotelPhoto from "../images/hotel.jpg";
import hotelPhoto2 from "../images/hotel2.jpg";
import hotelPhoto3 from "../images/hotel3.jpg";
import hotelPhoto4 from "../images/hotel4.jpg";
import hotelPhoto5 from "../images/hotel5.jpg";
import hotelPhoto6 from "../images/hotel6.jpg";
import hotelPhoto7 from "../images/hotel7.jpg";
import hotelPhoto8 from "../images/hotel8.jpg";
import hotelPhoto9 from "../images/hotel9.jpg";
import hotelPhoto10 from "../images/hotel10.jpg";

const FilterComponent = ({filterText, onFilter, placeholderText}) => (
    <>
        <Form>
            <Form.Control type="text" value={filterText} onChange={onFilter} placeholder={placeholderText}/>
        </Form>
    </>
);


function HotelList(props) {
    const {t, i18n} = props
    const history = useHistory()
    const {token, setToken, currentRole} = useLocale();
    const [filterText, setFilterText] = React.useState('');
    const themeColor = useThemeColor()
    const [etag, setETag] = useState();
    const [searchTerm, setSearchTerm] = React.useState('')
    const [data, setData] = useState([
        {
            id: "",
            name: "",
            address: "",
            cityName: "",
            rating: "",
            image: "",
            description: ""
        }
    ]);
    const dispatchDialog = useDialogPermanentChange();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const filteredItems = data.filter(item => {
        return item.name && item.name.toLowerCase().includes(filterText.toLowerCase());
    });

    const rand = () => {
        const min = 1;
        const max = 10;
        const rand = min + Math.floor(Math.random() * (max - min));
        switch(rand) {
            case 1:
                return hotelPhoto;
            case 2:
                return hotelPhoto2;
            case 3:
                return hotelPhoto3;
            case 4:
                return hotelPhoto4;
            case 5:
                return hotelPhoto5;
            case 6:
                return hotelPhoto6;
            case 7:
                return hotelPhoto7;
            case 8:
                return hotelPhoto8;
            case 9:
                return hotelPhoto9;
            case 10:
                return hotelPhoto10;
            default:
                return hotelPhoto;
        }
    }

    const HotelCard = ({hotel}) => (
        <Col xs={12} md={6} lg={3} key={hotel.id}>
            {themeColor === "light" ? (
                <Card style={{width: '16rem', background: '#f5f5f5', marginTop: '2rem'}}>
                    <Card.Img variant="top" src={hotel.image ? (hotel.image) : (rand())}/>
                    <Card.Body>
                        <Card.Title>{hotel.name}</Card.Title>
                        <Card.Text>{hotel.cityName}</Card.Text>
                        <Button className="btn-sm" onClick={event => {
                            history.push('/hotels/hotelInfo?id=' + hotel.id);
                        }}>{t('details')}</Button>
                    </Card.Body>
                </Card>
            ) : (
                <Card style={{width: '16rem', background: '#2b2b2b', marginTop: '2rem'}}>
                    <Card.Img variant="top" src={hotel.image ? (hotel.image) : (rand())}/>
                    <Card.Body>
                        <Card.Title>{hotel.name}</Card.Title>
                        <Card.Text>{hotel.cityName}</Card.Text>
                        <Button className="btn-sm" onClick={event => {
                            history.push('/hotels/hotelInfo?id=' + hotel.id);
                        }}>{t('details')}</Button>
                    </Card.Body>
                </Card>
            )}
        </Col>
    )

    const getHotelData = async (id) => {
        const response = await api.getHotel(id,{
            method: "GET",
            headers: {
                Authorization: token,
            }})
        return response;
    };

    const handleSearchTermChange = (event) => {
        setSearchTerm(event.target.value)
        if (event.target.value !== '') {
            fetchSearchedData(event.target.value)
        }
        else {
            fetchData()
        }
    }

    const fetchSearchedData = (query) => {
        axios.get(`${process.env.REACT_APP_API_BASE_URL}/resources/hotels/look/${query}`)
            .then(res => {
                setData(res.data)
            }).catch(res => {
            if (res.response != null) {
                if (res.response.status === 403) {
                    history.push("/errors/forbidden")
                } else if (res.response.status === 500) {
                    history.push("/errors/internal")
                }
            }
        });
    }

    const guestColumns = [
        {
            name: t('hotelName'),
            selector: 'name',
            width: "10rem"
        },
        {
            name: t('address'),
            selector: 'address',
            width: "10rem"
        },
        {
            name: t('city'),
            selector: 'cityName',
            width: "10rem"
        },
        {
            name: t('rating'),
            selector: 'rating'
        },
        {
            name: t('details'),
            selector: 'details',
            cell: row => {
                return(
                    <Button className="btn-sm" onClick={event => {
                        history.push('/hotels/hotelInfo?id=' + row.id);
                    }}>{t('details')}</Button>
                )
            }
        },
    ];

    const managerColumns = [
        {
            name: t('hotelName'),
            selector: 'name',
            width: "10rem"
        },
        {
            name: t('address'),
            selector: 'address',
            width: "10rem"
        },
        {
            name: t('city'),
            selector: 'cityName',
            width: "10rem"
        },
        {
            name: t('rating'),
            selector: 'rating'
        },
        {
            name: t('details'),
            selector: 'details',
            cell: row => {
                return(
                    <Button className="btn-sm" onClick={event => {
                        history.push('/hotels/hotelInfo?id=' + row.id);
                    }}>{t('details')}</Button>
                )
            }
        },
    ];

    const adminColumns = [
        {
            name: t('hotelName'),
            selector: 'name',
            width: "10rem"
        },
        {
            name: t('address'),
            selector: 'address',
            width: "10rem"
        },
        {
            name: t('city'),
            selector: 'cityName',
            width: "10rem"
        },
        {
            name: t('rating'),
            selector: 'rating'
        },
        {
            name: t('edit'),
            selector: 'edit',
            cell: row => {
                return (
                    <Button className="btn-sm" onClick={event => {
                        history.push('/hotels/editOtherHotel?id=' + row.id);
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
                        history.push('/hotels/hotelInfo?id=' + row.id);
                    }}>{t('details')}</Button>
                )
            }
        },
        {
            name: t('assignManager'),
            selector: 'assign',
            cell: row => {
                return (
                    <Button className="btn-sm" onClick={event => {
                        history.push('/hotels/assignManager?id=' + row.id);
                    }}>{t("assign")}</Button>
                )
            },
        },
    ];

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        getAllHotels().then(r => {
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

    const getAllHotels = async () => {
        return await api.getAllHotelsList({headers: {Authorization: token}})
    }

    const subHeaderComponentMemo = React.useMemo(() => {

        return <FilterComponent onFilter={e => {
            setFilterText(e.target.value);
        }} filterText={filterText} placeholderText={t('filterPhase')}/>;
    }, [filterText]);

    return (
        <div className="container">
            {token !== null && token !== '' ? (
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
                    <li className="breadcrumb-item active" aria-current="page">{t('hotelList')}</li>
                </BreadCrumb>
            ) : (
                <BreadCrumb>
                    <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                    <li className="breadcrumb-item active" aria-current="page">{t('hotelList')}</li>
                </BreadCrumb>
            )}
            <div className="floating-box">
                <div>
                    <h1 className="float-left">{t('hotelList')}</h1>
                    <Button className="btn-secondary float-right m-2" onClick={event => {
                        getAllHotels().then(res => {
                            setData(res.data);
                            setFilterText('')
                            dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
                        }).catch(err => {
                            ResponseErrorHandler(err, dispatchNotificationDanger)
                        })
                    }}>{t("refresh")}</Button>
                    {token !== null && token !== '' && currentRole === rolesConstant.admin ? (
                        <Button className="btn-primary float-right m-2" onClick={event => {
                            history.push('/hotels/addHotel');
                        }}>{t("addHotel")}</Button>
                    ) : ( null )}
                    <input
                        className="input float-right m-2"
                        type="text"
                        placeholder={t("search.hotel")}
                        value={searchTerm}
                        onChange={handleSearchTermChange}
                    />
                </div>
                {token === null || token === '' ? (
                    <div style={{height: '35rem', display: 'flex', flex: '1', flexDirection: 'row', width: '75rem', overflowY: 'scroll'}}>
                        <div className='row-wrapper' style={{padding: '1rem'}}>
                            <Row>
                                {data.map(hotel => (
                                    <HotelCard key={hotel.id} hotel={hotel}/>
                                ))}
                            </Row>
                        </div>
                    </div>
                ) : ( null )}
                {token !== null && token !== '' && currentRole === rolesConstant.client ? (
                    <DataTable className={"rounded-0"}
                               noDataComponent={i18n.t('table.no.result')}
                               columns={guestColumns}
                               data={filteredItems}
                               subHeader
                               theme={themeColor}
                    />
                ) : ( null )}
                {token !== null && token !== '' && currentRole === rolesConstant.manager ? (
                    <DataTable className={"rounded-0"}
                               noDataComponent={i18n.t('table.no.result')}
                               columns={managerColumns}
                               data={filteredItems}
                               subHeader
                               theme={themeColor}
                    />
                ) : ( null )}
                {token !== null && token !== '' && currentRole === rolesConstant.admin ? (
                    <DataTable className={"rounded-0"}
                               noDataComponent={i18n.t('table.no.result')}
                               columns={adminColumns}
                               data={filteredItems}
                               subHeader
                               theme={themeColor}
                    />
                ) : ( null )}
            </div>
        </div>
    )
}
export default withNamespaces()(HotelList);
