import React, {useState} from 'react';
import i18n from "../../i18n";
import DataTable from "react-data-table-component";
import moment from "moment";
import {useThemeColor} from "../Utils/ThemeColor/ThemeColorProvider";

function ReportsTable({bookings}) {
    const themeColor = useThemeColor()

    const momentHelper = () => {
        return i18n.language === "pl" ? 'pl' : 'en';
    }

    const columns = [
        {
            name: 'Id',
            selector: 'id',
            sortable: true,
            width: '5rem'
        },
        {
            name: i18n.t('reportGenerator.from'),
            selector: 'dateFrom',
            sortable: true,
            style: ({textTransform: 'capitalize'}),
            cell: row => (row.dateFrom ?
                moment(row.dateFrom).locale(momentHelper()).local().format('LLLL').toString() : "")
        },
        {
            name: i18n.t('reportGenerator.to'),
            selector: 'dateTo',
            sortable: true,
            style: ({textTransform: 'capitalize'}),
            cell: row => (row.dateTo ?
                moment(row.dateTo).locale(momentHelper()).local().format('LLLL').toString() : "")
        },
        {
            name: i18n.t('reportGenerator.modificationDate'),
            selector: 'modificationDate',
            sortable: true,
            style: ({textTransform: 'capitalize'}),
            sortFunction: (a, b) => (a.modificationDate - b.modificationDate),
            cell: row => (row.modificationDate ?
                moment(row.modificationDate).locale(momentHelper()).local().format('LLLL').toString() : "")
        },
        {
            name: 'Status',
            selector: 'status',
            cell: row => (row.status ? i18n.t(row.status.toLowerCase() + "BookingStatus") : "")
        },
        {
            name: i18n.t('reportGenerator.table.price'),
            selector: 'price',
            sortable: true,
            cell: row => (row.price ? row.price.toFixed(2) + " " + i18n.t('currency') : "")
        },
        {
            name: i18n.t('reportGenerator.table.rating'),
            selector: 'rating',
            sortable: true,
            sortFunction: (a, b) => (a.rating - b.rating)
        },
        {
            name: i18n.t('reportGenerator.table.owner_login'),
            selector: 'ownerLogin'
        },
    ];

    return (
        <DataTable className={"mt-0 pt-0 rounded-0"}
                   style={{overflowY: "auto"}}
                   title={i18n.t('reportGenerator.table.title')}
                   noDataComponent={i18n.t('table.no.result')}
                   data={bookings}
                   highlightOnHover
                   columns={columns}
                   theme={themeColor === 'light' ? 'lightMode' : themeColor}/>
    );
}

export default ReportsTable;