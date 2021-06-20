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
import Select from 'react-select';
import {animalTypes, queryBuilder} from "./Utils/HotelsView/AnimalTypes";
import {sortingTypes} from "./Utils/HotelsView/SortingTypes";

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
    const [searchTerm, setSearchTerm] = React.useState('');
    const [selectedValue, setSelectedValue] = useState([]);
    const [sortSelectedValue, setSortSelectedValue] = useState();
    const [minRatingValue, setMinRatingValue] = useState();
    const [maxRatingValue, setMaxRatingValue] = useState();
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

    const HotelCard = ({hotel}) => (
        <Col xs={12} md={6} lg={3} key={hotel.id}>
            {themeColor === "light" ? (
                <Card style={{width: '16rem', background: '#f5f5f5', marginRight: '20rem', marginTop: '2rem'}}>
                    <Card.Img variant="top" src={hotel.image ? (hotel.image) : (hotelPhoto)}/>
                    {hotel.name ? (
                        <Card.Body>
                            <Card.Title>{hotel.name}</Card.Title>
                            <Card.Text>{hotel.cityName}</Card.Text>
                            <Button className="btn-sm" onClick={event => {
                                history.push('/hotels/hotelInfo?id=' + hotel.id);
                            }}>{t('details')}</Button>
                        </Card.Body>
                    ) : (
                        <Card.Body>
                            <Card.Title>{t('emptyListHotel')}</Card.Title>
                        </Card.Body>
                    )}
                </Card>
            ) : (
                <Card style={{width: '16rem', background: '#2b2b2b', marginRight: '20rem', marginTop: '2rem'}}>
                    <Card.Img variant="top" src={hotel.image ? (hotel.image) : (hotelPhoto)}/>
                    {hotel.name ? (
                        <Card.Body>
                            <Card.Title>{hotel.name}</Card.Title>
                            <Card.Text>{hotel.cityName}</Card.Text>
                            <Button className="btn-sm" onClick={event => {
                                history.push('/hotels/hotelInfo?id=' + hotel.id);
                            }}>{t('details')}</Button>
                        </Card.Body>
                    ) : (
                        <Card.Body>
                            <Card.Title>{t('emptyListHotel')}</Card.Title>
                        </Card.Body>
                    )}
                </Card>
            )}
        </Col>
    )

    const handleSearchTermChange = (event) => {
        setSearchTerm(event.target.value)
        setMinRatingValue('')
        setMaxRatingValue('')
        setSelectedValue([])
        if (event.target.value !== '') {
            fetchSearchedData(event.target.value)
            setSortSelectedValue('')
        }
        else {
            fetchData()
        }
    }

    const handleSelectedValueChange = (e) => {
        setSelectedValue(Array.isArray(e) ? e.map(x => x.value) : []);
    }

    const handleSelectedSortValueChange = (e) => {
        setSortSelectedValue(e);
        sortHotelData(e.value - 1)
    }

    const sortHotelData = (property) => {
        const types = [
            'name',
            'rating'
        ];
        let sorted
        if (property === 0) {
            const sortProperty = types[property];
            sorted = [...data].sort((a, b) => {
                if (a[sortProperty] < b[sortProperty]) return -1;
                if (a[sortProperty] > b[sortProperty]) return 1;
                return 0;
            })
        }
        else if (property === 1) {
            const sortProperty = types[property];
            sorted = [...data].sort((a, b) => {
                if (a[sortProperty] === undefined) return 1;
                if (b[sortProperty] === undefined) return -1;
                if (b[sortProperty] < a[sortProperty]) return -1;
                if (b[sortProperty] > a[sortProperty]) return 1;
                return 0;
            })
        }
        else if (property === 2){
            const sortProperty = types[property - 1]
            sorted = [...data].sort((a, b) => {
                if (b[sortProperty] > a[sortProperty]) return -1;
                if (b[sortProperty] < a[sortProperty]) return 1;
                if (a[sortProperty] === undefined) return 1;
                if (b[sortProperty] === undefined) return -1;
                return 0
            })
        }
        setData(sorted);
    }

    const handleFilterClick = () => {
        let query = queryBuilder(minRatingValue, maxRatingValue, selectedValue, searchTerm)

        axios.get(`${process.env.REACT_APP_API_BASE_URL}/resources/hotels/filter` + query)
            .then(res => {
                setData(res.data)
                setSortSelectedValue('')
            })
            .catch(err => {
                ResponseErrorHandler(err, dispatchNotificationDanger)
            })
    }

    const handleMinValueChange = (e) => {
        setMinRatingValue(e.target.value)
    }

    const handleMaxValueChange = (e) => {
        setMaxRatingValue(e.target.value)
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
        {
            name: t('deleteManager'),
            selector: 'delete',
            cell: row => {
                return(
                    <Button className="btn-sm" onClick={event => {
                        history.push('/hotels/unassignManager?id=' + row.id);
                    }}>{t("delete")}</Button>
                )
            }
        },
    ];

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        getAllHotels().then(r => {
            console.log(r);
            setData(r.data);
            setSortSelectedValue('')
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
                    <h1>{t('hotelList')}</h1>
                </div>
                <div className="d-flex flex-row-reverse">
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
                        style={themeColor === "light" ? ({backgroundColor: "#f8f9fa"}) : ({color: "#f8f9fa", backgroundColor: "#424242"})}
                    />
                    <Select
                        className="align-self-center w-25"
                        placeholder='...'
                        value={sortSelectedValue}
                        options={sortingTypes}
                        onChange={handleSelectedSortValueChange}
                    />
                    <h4
                        className="float-right align-self-center">
                        {t('sort.by')}
                    </h4>
                </div>
                <div className="d-flex flex-row-reverse">
                    <Button
                        onClick={handleFilterClick}
                        className="btn-secondary float-right m-2">
                        {t('filter.button')}
                    </Button>
                    <Select
                        className="float-right dropdown align-self-center w-25"
                        placeholder={t("choose.animal.type")}
                        value={animalTypes.filter(obj => selectedValue.includes(obj.value))}
                        options={animalTypes}
                        onChange={handleSelectedValueChange}
                        isMulti
                        isClearable
                    />
                    <h4
                        className="float-right align-self-center">
                        {t('text.animal.type')}
                    </h4>
                    <input
                        className="input float-right m-2"
                        type="number"
                        step="0.1"
                        min='1'
                        max='5'
                        placeholder={t('rating.maximal')}
                        value={maxRatingValue}
                        onChange={handleMaxValueChange}
                    />
                    <input
                        className="input float-right m-2"
                        type="number"
                        step='0.1'
                        min='1'
                        max='5'
                        placeholder={t('rating.minimal')}
                        value={minRatingValue}
                        onChange={handleMinValueChange}
                    />
                    <h4
                        className="float-right align-self-center">
                        {t('rating')}
                    </h4>
                </div>
                {(token === null || token === '') &&
                    <div style={{height: '35rem', display: 'flex', flex: '1', flexDirection: 'row', width: '75rem', overflowY: 'scroll'}}>
                        <div className='row-wrapper' style={{padding: '1rem'}}>
                            <Row>
                                {data.map(hotel => (
                                    <HotelCard key={hotel.id} hotel={hotel}/>
                                ))}
                            </Row>
                        </div>
                    </div>
                }
                {(token !== null && token !== '' && currentRole === rolesConstant.client) &&
                    <div style={{height: '35rem', display: 'flex', flex: '1', flexDirection: 'row', width: '75rem', overflowY: 'scroll'}}>
                        <div className='row-wrapper' style={{padding: '1rem'}}>
                            <Row>
                                {data.map(hotel => (
                                    <HotelCard key={hotel.id} hotel={hotel}/>
                                ))}
                            </Row>
                        </div>
                    </div>
                }
                {(token !== null && token !== '' && currentRole === rolesConstant.manager) &&
                    <DataTable className={"rounded-0"}
                               noDataComponent={i18n.t('table.no.result')}
                               columns={managerColumns}
                               data={filteredItems}
                               subHeader
                               theme={themeColor}
                               subHeaderComponent={subHeaderComponentMemo}
                    />
                }
                {(token !== null && token !== '' && currentRole === rolesConstant.admin) &&
                    <DataTable className={"rounded-0"}
                               noDataComponent={i18n.t('table.no.result')}
                               columns={adminColumns}
                               data={filteredItems}
                               subHeader
                               theme={themeColor}
                               subHeaderComponent={subHeaderComponentMemo}
                    />
                }
            </div>
        </div>
    )
}
export default withNamespaces()(HotelList);
