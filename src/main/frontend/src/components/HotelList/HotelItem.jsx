import React from "react";
import {Button, Card} from "react-bootstrap";
import i18n from '../../i18n';
import hotelPhoto from "../../images/hotel.jpg";
import {Rating} from "@material-ui/lab";
import {useHistory} from "react-router";

function HotelItem(props) {
    const {hotel} = props;
    const history = useHistory()

    return (
        <Card className={"hotel-item"}>
            <Card.Img className={"p-2"} variant="top" src={hotel.image ? (hotel.image) : (hotelPhoto)}/>
            {hotel.name ? (
                <Card.Body className={"hotel-item-body"}>
                    <Card.Title>{hotel.name}</Card.Title>
                    <Card.Text>{hotel.cityName}</Card.Text>
                    <Rating
                        defaultValue={0}
                        value={hotel.rating}
                        readOnly
                        precision={0.1}
                        size="small"
                    />
                    <>
                        <Button className={"btn-block card-button ml-0"} style={{backgroundColor: "#7749F8"}} onClick={event => {
                            history.push('/hotels/hotelInfo?id=' + hotel.id);
                        }}>{i18n.t('details')}</Button>
                    </>
                </Card.Body>
            ) : (
                <Card.Body className={"hotel-item-body"}>
                    <Card.Title>{i18n.t('emptyListHotel')}</Card.Title>
                </Card.Body>
            )}
        </Card>
    );
}

export default HotelItem;