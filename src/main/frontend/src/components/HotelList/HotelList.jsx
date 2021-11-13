import {withNamespaces} from 'react-i18next';
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import DataTable, {createTheme} from "react-data-table-component"
import {Button, Col, Container, Row} from "react-bootstrap";
import {useLocale} from "../LoginContext";
import {api} from "../../Api";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {useNotificationDangerAndInfinity, useNotificationSuccessAndShort} from "../Utils/Notification/NotificationProvider";
import {useHistory} from "react-router";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {useThemeColor} from '../Utils/ThemeColor/ThemeColorProvider';
import {rolesConstant} from "../../Constants";
import axios from "axios";
import Select from 'react-select';
import {animalTypes, queryBuilder} from "../Utils/HotelsView/AnimalTypes";
import {sortingTypes} from "../Utils/HotelsView/SortingTypes";
import HotelItem from "./HotelItem";
import "./HotelListStyle.scss"


function HotelList(props) {
    const {t, i18n} = props
    const history = useHistory()
    const {token, setToken, currentRole} = useLocale();
    const [filterText, setFilterText] = React.useState('');
    const themeColor = useThemeColor()
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

    const selectStyles = {
        option: (styles, state) => {
            if (themeColor === "dark") {
                return {
                    ...styles,
                    backgroundColor: state.isFocused ? "#424242" : "",
                    color: state.isFocused ? "#f8f9fa" : "#424242"
                }
            } else {
                return {
                    ...styles,
                    backgroundColor: state.isFocused ? "#7749F8FF" : "#f8f9fa",
                    color: state.isFocused ? "#f8f9fa" : "#424242"
                }
            }
        },
        multiValue: styles => {
            if (themeColor === "dark") {
                return {
                    ...styles,
                    backgroundColor: "#a8a4a4",
                    color: "#f8f9fa",

                }
            } else {
                return {
                    ...styles,
                    backgroundColor: "#f8f9fa",
                    color: "#a8a4a4"
                }
            }
        },
        singleValue: styles => {
            if (themeColor === "dark") {
                return {
                    ...styles,
                    color: "#f8f9fa",

                }
            } else {
                return {
                    ...styles,
                    color: "#a8a4a4"
                }
            }
        },
        control: styles => {
            if (themeColor === "dark") {
                return {
                    ...styles,
                    backgroundColor: "#424242",
                    color: "#f8f9fa",
                    minWidth: "100px",
                }
            } else {
                return {
                    ...styles,
                    backgroundColor: "#f8f9fa",
                    minWidth: "100px",
                }
            }
        }
    }

    const handleSearchTermChange = (event) => {
        setSearchTerm(event.target.value)
        setMinRatingValue('')
        setMaxRatingValue('')
        setSelectedValue([])
        if (event.target.value !== '') {
            fetchSearchedData(event.target.value)
        } else {
            fetchData()
        }
    }

    const handleSelectedValueChange = (e) => {
        setSelectedValue(Array.isArray(e) ? e.map(x => x.value) : []);
    }

    const handleSelectedSortValueChange = (e) => {
        setSortSelectedValue(e);
        sortHotelData(e.value - 1, data)
    }

    const sortHotelData = (property, sortData) => {
        const types = [
            'name',
            'rating'
        ];
        let sorted
        if (property === 0) {
            const sortProperty = types[property];
            sorted = [...sortData].sort((a, b) => {
                if (a[sortProperty] < b[sortProperty]) return -1;
                if (a[sortProperty] > b[sortProperty]) return 1;
                return 0;
            })
        } else if (property === 1) {
            const sortProperty = types[property];
            sorted = [...sortData].sort((a, b) => {
                if (a[sortProperty] === undefined) return 1;
                if (b[sortProperty] === undefined) return -1;
                if (b[sortProperty] < a[sortProperty]) return -1;
                if (b[sortProperty] > a[sortProperty]) return 1;
                return 0;
            })
        } else if (property === 2) {
            const sortProperty = types[property - 1]
            sorted = [...sortData].sort((a, b) => {
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

        axios.get(`${document.location.origin}/resources/hotels/filter` + query)
            .then(res => {
                if (sortSelectedValue !== undefined && sortSelectedValue !== '') {
                    sortHotelData(sortSelectedValue.value - 1, res.data)
                } else {
                    setData(res.data)
                }
            })
            .catch(err => {
                ResponseErrorHandler(err, dispatchNotificationDanger)
            })
    }

    const handleMinValueChange = (e) => {
        let {value, min, max} = e.target;
        if (value !== '') {
            value = Math.max(Number(min), Math.min(Number(max), Number(value)));
        }
        setMinRatingValue(value)
    }

    const handleMaxValueChange = (e) => {
        let {value, min, max} = e.target;
        if (value !== '') {
            value = Math.max(Number(min), Math.min(Number(max), Number(value)));
        }
        setMaxRatingValue(value)
    }

    const fetchSearchedData = (query) => {
        axios.get(`${document.location.origin}/resources/hotels/look/${query}`)
            .then(res => {
                if (sortSelectedValue !== undefined && sortSelectedValue !== '') {
                    sortHotelData(sortSelectedValue.value - 1, res.data)
                } else {
                    setData(res.data)
                }
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
            width: "15rem"
        },
        {
            name: t('address'),
            selector: 'address',
            width: "15rem"
        },
        {
            name: t('city'),
            selector: 'cityName',
            width: "15rem"
        },
        {
            name: t('rating'),
            selector: 'rating',
            width: "10rem"
        },
        {
            name: t('details'),
            selector: 'details',
            cell: row => {
                return (
                    <Button className="btn-sm" variant="purple" onClick={event => {
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
                    <Button className="btn-sm" variant="purple" onClick={event => {
                        history.push('/hotels/editOtherHotel?id=' + row.id);
                    }}>{t("edit")}</Button>
                )
            },
        },
        {
            name: t('details'),
            selector: 'details',
            cell: row => {
                return (
                    <Button className="btn-sm" variant="purple" onClick={event => {
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
                    <Button className="btn-sm" variant="purple" onClick={event => {
                        history.push('/hotels/assignManager?id=' + row.id);
                    }}>{t("assign")}</Button>
                )
            },
        },
        {
            name: t('deleteManager'),
            selector: 'delete',
            cell: row => {
                return (
                    <Button className="btn-sm" variant="purple" onClick={event => {
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
            if (sortSelectedValue !== undefined && sortSelectedValue !== '') {
                sortHotelData(sortSelectedValue.value - 1, r.data)
            }
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

    return (
        <div className="container-fluid mb-2">
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
            <Container>
                <Row>
                    <Col xs={12} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div>
                            <div>
                                <h1>{t('hotelList')}</h1>
                            </div>
                            <div className="d-flex flex-column">
                                <div className="d-flex float-right flex-wrap">
                                    <h4
                                        className="float-right align-self-center mr-1">
                                        {t('sort.by')}
                                    </h4>
                                    <Select
                                        className="align-self-center w-25"
                                        placeholder=''
                                        value={sortSelectedValue}
                                        options={sortingTypes}
                                        styles={selectStyles}
                                        onChange={handleSelectedSortValueChange}
                                    />
                                    <input
                                        className="input m-2 w-25"
                                        type="text"
                                        placeholder={t("search.hotel")}
                                        value={searchTerm}
                                        onChange={handleSearchTermChange}
                                        style={themeColor === "light" ? ({backgroundColor: "#f8f9fa"}) : ({
                                            color: "#f8f9fa",
                                            backgroundColor: "#424242"
                                        })}
                                    />
                                    {token !== null && token !== '' && currentRole === rolesConstant.admin ? (
                                        <Button className="btn-secondary float-right m-2"
                                            onClick={event => {
                                            history.push('/hotels/addHotel');
                                        }}>{t("addHotel")}</Button>
                                    ) : null}
                                    <Button className="btn-secondary float-right m-2" onClick={event => {
                                        getAllHotels().then(res => {
                                            setData(res.data);
                                            setFilterText('')
                                            dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
                                        }).catch(err => {
                                            ResponseErrorHandler(err, dispatchNotificationDanger)
                                        })
                                    }}>{t("refresh")}</Button>
                                </div>
                                <div className="d-flex float-right flex-wrap">
                                    <h4
                                        className="align-self-center">
                                        {t('rating')}
                                    </h4>
                                    <input
                                        className="input m-2"
                                        type="number"
                                        step='0.1'
                                        min='1'
                                        max='5'
                                        placeholder={t('rating.minimal')}
                                        value={minRatingValue}
                                        onChange={handleMinValueChange}
                                        style={themeColor === "light" ? ({
                                            backgroundColor: "#f8f9fa",
                                            minWidth: "10%"
                                        }) : ({color: "#f8f9fa", backgroundColor: "#424242", minWidth: "10%"})}
                                    />
                                    <input
                                        className="input m-2"
                                        type="number"
                                        step="0.1"
                                        min='1'
                                        max='5'
                                        placeholder={t('rating.maximal')}
                                        value={maxRatingValue}
                                        onChange={handleMaxValueChange}
                                        style={themeColor === "light" ? ({
                                            backgroundColor: "#f8f9fa",
                                            minWidth: "10%"
                                        }) : ({color: "#f8f9fa", backgroundColor: "#424242", minWidth: "10%"})}
                                    />
                                    <h4
                                        className="align-self-center mr-1">
                                        {t('text.animal.type')}
                                    </h4>
                                    <Select
                                        className="dropdown align-self-center"
                                        placeholder=''
                                        value={animalTypes.filter(obj => selectedValue.includes(obj.value))}
                                        options={animalTypes}
                                        onChange={handleSelectedValueChange}
                                        styles={selectStyles}
                                        closeMenuOnSelect={false}
                                        isMulti
                                        isClearable
                                    />
                                    <Button
                                        onClick={handleFilterClick}
                                        className="btn-secondary float-right m-2">
                                        {t('filter.button')}
                                    </Button>
                                </div>
                            </div>
                            {(token === null || token === '') &&
                            <div id={"hotel-list"} className={"container-fluid"}>
                                <div style={{
                                    display: 'flex',
                                    flex: '1',
                                    flexDirection: 'row'
                                }}>
                                    <>
                                        <div className='row-wrapper w-100' style={{padding: '1rem'}}>
                                            <div className={"row"} style={{display: "flex"}}>
                                                {data.map(hotel => (
                                                    <div style={{display: "flex"}} className={"col-sm-6 col-md-3 my-2"}>
                                                        <HotelItem key={hotel.id} hotel={hotel}/>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    </>
                                </div>
                            </div>
                            }
                            {(token !== null && token !== '' && currentRole === rolesConstant.client) &&
                            <div id={"hotel-list"} className={"container-fluid"}>
                                <div style={{
                                    display: 'flex',
                                    flex: '1',
                                    flexDirection: 'row',
                                }}>
                                    <>
                                        <div className='row-wrapper w-100' style={{padding: '1rem'}}>
                                            <div className={"row"} style={{display: "flex"}}>
                                                {data.map(hotel => (
                                                    <div style={{display: "flex"}} className={"col-sm-6 col-md-3 my-2"}>
                                                        <HotelItem key={hotel.id} hotel={hotel}/>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    </>
                                </div>
                            </div>
                            }
                            {(token !== null && token !== '' && currentRole === rolesConstant.manager) &&
                            <DataTable className={"rounded-0"}
                                       noDataComponent={i18n.t('table.no.result')}
                                       columns={managerColumns}
                                       data={filteredItems}
                                       theme={themeColor === 'light' ? 'lightMode' : themeColor}
                                       noHeader={true}
                            />
                            }
                            {(token !== null && token !== '' && currentRole === rolesConstant.admin) &&
                            <DataTable className={"rounded-0"}
                                       noDataComponent={i18n.t('table.no.result')}
                                       columns={adminColumns}
                                       data={filteredItems}
                                       theme={themeColor === 'light' ? 'lightMode' : themeColor}
                                       noHeader={true}
                            />
                            }
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default withNamespaces()(HotelList);