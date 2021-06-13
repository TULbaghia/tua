import React from "react";
import {Rating} from "@material-ui/lab";
import {Card} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faEllipsisV} from '@fortawesome/free-solid-svg-icons'


export default function RatingComponent({rate, login, content, date}) {
    return (
        <>
            <Card className="mb-4">
                <Card.Header className={"d-flex justify-content-between text-left p-1 align-items-center"}>
                    <Rating
                        size={"large"}
                        value={rate}
                        readOnly
                        precision={0.5}
                    /><FontAwesomeIcon className={"float-right mr-2"} icon={faEllipsisV} size={"lg"}/></Card.Header>
                <Card.Body>
                    <Card.Text className={"text-justify"}>
                        {content}
                    </Card.Text>
                </Card.Body>
                <Card.Footer className={"p-2"}><span className={"float-right text-muted"}>{date}</span><span
                    className={"float-right mr-2"}>{login}</span></Card.Footer>
            </Card>
        </>
    )
}