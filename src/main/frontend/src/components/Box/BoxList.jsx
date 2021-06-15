import React, {Component, useEffect, useState} from "react";
import {useLocale} from "../LoginContext";
import BoxItem from "./BoxItem";
import "./BoxListStyle.scss"
import {withNamespaces} from "react-i18next";

function BoxList(props) {

    const [boxes, setBoxes] = useState([])
    const {token} = useLocale();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: token,
            },
        };

        fetch("/resources/boxes", requestOptions)
            .then((res) => res.json())
            .then(
                (boxes) => {
                    setBoxes(boxes);
                },
                (error) => {
                    console.log(error);
                }
            );
    }

    const handleDelete = (boxId) => {
        setBoxes(boxes.filter((b) => b.id !== boxId))
    };

    const {onModify} = props;

    return (
        <div className={"my-5 row"}>
            {boxes.map((box) => (
                <div style={{display: "flex"}} className={"col-md-3 my-2"}>
                    <BoxItem
                        key={box.id}
                        onDelete={handleDelete}
                        onModify={onModify}
                        box={box}
                    />
                </div>
            ))}
        </div>
    );
}

function Boxes(props) {

    const handleModify = (userId) => {
        props.history.push({
            pathname: "/",
            state: {idOfUser: userId},
        });
    };

    return (
        <div id={"box-list"} className={"container"}>
            <BoxList onModify={handleModify}/>
        </div>
    );
}

export default withNamespaces()(Boxes);
