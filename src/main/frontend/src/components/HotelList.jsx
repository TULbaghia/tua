import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./Partial/BreadCrumb";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import DataTable from "react-data-table-component"
import {Button, Form} from "react-bootstrap";
import {useLocale} from "./LoginContext";
import {api} from "../Api";
import {useDialogPermanentChange} from "./Utils/CriticalOperations/CriticalOperationProvider";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "./Utils/Notification/NotificationProvider";
import {useHistory, useLocation} from "react-router";
import {ResponseErrorHandler} from "./Validation/ResponseErrorHandler";
import { useThemeColor } from './Utils/ThemeColor/ThemeColorProvider';
import {rolesConstant} from "../Constants";

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
    const [data, setData] = useState([
        {
            id: "",
            name: "",
            address: "",
            cityName: "",
            rating: "",
        }
    ]);
    const dispatchDialog = useDialogPermanentChange();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const filteredItems = data.filter(item => {
        return item.name && item.name.toLowerCase().includes(filterText.toLowerCase());
    });

    const getHotelData = async (id) => {
        const response = await api.getHotel(id,{
            method: "GET",
            headers: {
                Authorization: token,
            }})
        setETag(response.headers.etag);
    };

    const deleteHotel = (id) => (
        getHotelData(id).then(res => {
                api.deleteHotel(id, {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: token,
                        "If-Match": etag
                    }
                }).then((res) => {
                    dispatchNotificationSuccess({message: i18n.t('hotelDelete.success')})
                }).catch(err => {
                    ResponseErrorHandler(err, dispatchNotificationDanger);
                }).finally(() => fetchData());
            }
        )
    )

    const handleDeleteHotel = id => {
        dispatchDialog({
            callbackOnSave: () => {
               deleteHotel(id)
            },
        })
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
                        history.push('/editHotel?id=' + row.id);
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
            name: t('delete'),
            selector: 'delete',
            cell: row => {
                return (
                    <Button className="btn-sm" onClick={() => handleDeleteHotel(row.id)}>{t("delete")}</Button>
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
        return await api.getAllHotels({headers: {Authorization: token}})
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
                        history.push('/addHotel');
                    }}>{t("addHotel")}</Button>
                    ) : ( null )}
                </div>
                {token === null || token === '' ? (
                    <DataTable className={"rounded-0"}
                               noDataComponent={i18n.t('table.no.result')}
                               columns={guestColumns}
                               data={filteredItems}
                               subHeader
                               theme={themeColor}
                    />
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
