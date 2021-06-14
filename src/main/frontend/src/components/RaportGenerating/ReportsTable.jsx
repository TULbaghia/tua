import React, {useState} from 'react';
import i18n from "../../i18n";
import DataTable from "react-data-table-component";
import moment from "moment";
import {useThemeColor} from "../Utils/ThemeColor/ThemeColorProvider";

function ReportsTable({bookings}) {
    const themeColor = useThemeColor()

    const momentHelper = () => {
        return i18n.language === "pl" ? "pl" : "en-gb";
    }

    const columns = [
        {
            name: 'Id',
            selector: 'id',
            sortable: true,
            width: "10rem"
        },
        {
            name: i18n.t('reportGenerator.from'),
            selector: 'dateFrom',
            sortable: true,
            width: "10rem",
            cell: row => (row.dateFrom ?
                moment(row.dateFrom).locale(momentHelper()).local().format('LLLL').toString() : "")
        },
        {
            name: i18n.t('reportGenerator.to'),
            selector: 'dateTo',
            sortable: true,
            width: "10rem",
            cell: row => (row.dateTo ?
                moment(row.dateTo).locale(momentHelper()).local().format('LLLL').toString() : "")
        },
        {
            name: 'Status',
            selector: 'status',
            sortable: true,
            width: "10rem"
        },
        {
            name: i18n.t('reportGenerator.table.price'),
            selector: 'price',
            sortable: true,
            width: "10rem"
        },
        {
            name: i18n.t('reportGenerator.table.rating'),
            selector: 'rating',
            sortable: true,
            width: "10rem"
        },
        {
            name: i18n.t('reportGenerator.table.owner_login'),
            selector: 'ownerLogin',
            sortable: true,
            width: "10rem"
        },
    ];

    return (
        <DataTable className={"mt-0 pt-0 rounded-0"}
                   title={i18n.t('reportGenerator.table.title')}
                   noDataComponent={i18n.t('table.no.result')}
                   data={bookings}
                   columns={columns}
                   theme={themeColor}/>
    );
}

export default ReportsTable;