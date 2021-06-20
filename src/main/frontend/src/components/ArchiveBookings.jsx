import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./Partial/BreadCrumb";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import DataTable from "react-data-table-component"
import {Button, Form, FormCheck} from "react-bootstrap";
import {useLocale} from "./LoginContext";
import {api} from "../Api";
import {useDialogPermanentChange} from "./Utils/CriticalOperations/CriticalOperationProvider";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "./Utils/Notification/NotificationProvider";
import {useHistory} from "react-router";
import {ResponseErrorHandler} from "./Validation/ResponseErrorHandler";
import { useThemeColor } from './Utils/ThemeColor/ThemeColorProvider';
import {dateConverter} from "../i18n";
import {rolesConstant} from "../Constants";
import axios from "axios";

const FilterComponent = ({filterText, onFilter, placeholderText}) => (
    <>
        <Form>
            <Form.Control type="text" value={filterText} onChange={onFilter} placeholder={placeholderText}/>
        </Form>
    </>
);


function ArchiveBookings(props) {
    const {t, i18n} = props
    const history = useHistory()
    const {token, setToken, currentRole, setCurrentRole} = useLocale();
    const [filterText, setFilterText] = React.useState('');
    const themeColor = useThemeColor()
    const dangerNotifier = useNotificationDangerAndInfinity();
    const [data, setData] = useState([
        {
            id: 0,
            dateFrom: "",
            dateTo: "",
            price: 0,
            bookingLineList: [],
            rating: 0,
            bookingStatus: "",
        }
    ]);
    const dispatchDialog = useDialogPermanentChange();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const filteredItems = data.filter(item => {
        return item.id && item.id.toString().includes(filterText);
    });

    const columns = [
        {
            name: 'Id',
            selector: 'id',
            sortable: true,
        },
        {
            name: t('reservationStart'),
            selector: 'dateFrom',
            sortable: true,
            cell: row => {
                return(
                    dateConverter(row.dateFrom.slice(0, -5))
                );
            }
        },
        {
            name: t('reservationEnd'),
            selector: 'dateTo',
            sortable: true,
            cell: row => {
                return(
                    dateConverter(row.dateTo.slice(0, -5))
                );
            }
        },
        {
            name: t('price'),
            selector: 'price',
            sortable: true,
            cell: row => {
                return row.price + " " + t('currency');
            }
        },
        {
            name: t('bookingStatus'),
            selector: 'bookingStatus',
            sortable: true,
            cell: row => {
                return(
                    t(row.bookingStatus.toLowerCase() + "BookingStatus")
                );
            }
        },
        {
            name: t('bookingDetails'),
            cell: row => {
                return (
                    <Button className="btn-sm" style={{backgroundColor: "#7749F8"}} onClick={event => {
                        history.push("/reservation/details/" + row.id + "?ref=archive");
                    }}>{t("bookingDetails.text")}</Button>
                );
            }
        },
    ];
    if (currentRole === rolesConstant.client) {
        columns.push({
            name: t('addRating'),
            cell: row => {
                return(
                    <Button className="btn-sm" style={{backgroundColor: "#7749F8"}} disabled={row.bookingStatus !== "FINISHED"} onClick={event => {
                        console.log("rating added to:" +  row.id);
                        getHotelForBooking(row.id).then(res => {
                            history.push('/hotels/hotelInfo?id=' + res.data.id)
                        }).catch((e) => ResponseErrorHandler(e, dangerNotifier));
                    }}>{t("add")}</Button>
                );
            }
        });
    }

    const getHotelForBooking = async (bookigId) => {
        return await axios.get(`${process.env.REACT_APP_API_BASE_URL}/resources/hotels/hotel/booking/` + bookigId, {headers: {
                Authorization: token,
            }})
    }

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        if (token) {
            getArchiveBookings().then(r => {
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
    }

    const getArchiveBookings = async () => {
        return await api.showEndedBooking({headers: {Authorization: token}})
    }


    const subHeaderComponentMemo = React.useMemo(() => {

        return <FilterComponent onFilter={e => {
            setFilterText(e.target.value);
        }} filterText={filterText} placeholderText={t('filterPhase')}/>;
    }, [filterText]);

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('archiveReservations')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <div>
                    <h1 className="float-left">{t('archiveReservations')}</h1>
                    <Button className="btn-secondary float-right m-2" onClick={event => {
                        getArchiveBookings().then(res => {
                            setData(res.data);
                            setFilterText('')
                            dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
                        }).catch(err => {
                            ResponseErrorHandler(err, dispatchNotificationDanger)
                        })
                    }}>{t("refresh")}</Button>
                </div>
                <DataTable className={"rounded-0"}
                           noDataComponent={i18n.t('table.no.result')}
                           columns={columns}
                           data={filteredItems}
                           subHeader
                           theme={themeColor}
                           subHeaderComponent={subHeaderComponentMemo}
                />
            </div>
        </div>
    )
}
export default withNamespaces()(ArchiveBookings);