import React, {useState} from 'react';
import i18n from "../../i18n";
import DataTable from "react-data-table-component";
import moment from "moment";
import {useThemeColor} from "../Utils/ThemeColor/ThemeColorProvider";

function ReportsTable({bookings}) {
    const [sortDir, setSortDir] = useState("asc");
    const themeColor = useThemeColor()

    const momentHelper = () => {
        return i18n.language === "pl" ? "pl" : "en-gb";
    }

    const columns = [
        {
            name: 'Id',
            selector: 'id',
            sortable: true,
            width: "4rem"
        },
        {
            name: i18n.t('reportGenerator.from'),
            selector: 'dateFrom',
            sortable: true,
            width: "10rem",
            style: ({textTransform: 'capitalize'}),
            cell: row => (row.dateFrom ?
                moment(row.dateFrom).locale(momentHelper()).local().format('LLLL').toString() : "")
        },
        {
            name: i18n.t('reportGenerator.to'),
            selector: 'dateTo',
            sortable: true,
            width: "10rem",
            style: ({textTransform: 'capitalize'}),
            cell: row => (row.dateTo ?
                moment(row.dateTo).locale(momentHelper()).local().format('LLLL').toString() : "")
        },
        {
            name: 'Status',
            selector: 'status',
            width: "10rem",
            cell: row => (row.status ? i18n.t(row.status.toLowerCase() + "BookingStatus") : "")
        },
        {
            name: i18n.t('reportGenerator.table.price'),
            selector: 'price',
            sortable: true,
            width: "10rem",
            cell: row => (row.price ? row.price + " " + i18n.t('currency') : "")
        },
        {
            name: i18n.t('reportGenerator.table.rating'),
            selector: 'rating',
            sortable: true,
            width: "10rem",
            sortFunction: (a, b) => {
                if (a.rating === undefined || b.rating === undefined) {
                    return sortDir === 'asc' ? -1 : 1;
                }
                return a.rating - b.rating;
            }
        },
        {
            name: i18n.t('reportGenerator.table.owner_login'),
            selector: 'ownerLogin',
            width: "10rem"
        },
    ];

    return (
        <DataTable className={"mt-0 pt-0 rounded-0"}
                   style={{overflowY: "auto", maxHeight: "15rem"}}
                   title={i18n.t('reportGenerator.table.title')}
                   noDataComponent={i18n.t('table.no.result')}
                   data={bookings}
                   columns={columns}
                   defaultSortField={"rating"}
                   onSort={(column, sortDirection) => setSortDir(sortDirection)}
                   theme={themeColor}/>
    );
}

export default ReportsTable;