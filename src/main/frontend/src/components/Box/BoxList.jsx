import React, {Component, useEffect, useState} from "react";
import {useLocale} from "../LoginContext";
import BoxItem from "./BoxItem";
import "./BoxListStyle.scss"
import {withNamespaces} from "react-i18next";

function BoxList(props) {

    const [boxes, setBoxes] = useState([])
    const {token, username} = useLocale();

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

        fetch("/resources/boxes/all/" + username, requestOptions)
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

    const handleModify = (userId) => {
        props.history.push({
            pathname: "/",
            state: {idOfBox: userId},
        });
    };

    const handleDelete = (boxId) => {
        setBoxes(boxes.filter((b) => b.id !== boxId))
    };

    return (
        <div id={"box-list"} className={"container"}>
            <div className={"my-5 row"}>
                {boxes.map((box) => (
                    <div style={{display: "flex"}} className={"col-md-3 my-2"}>
                        <BoxItem
                            key={box.id}
                            onDelete={handleDelete}
                            onModify={handleModify}
                            box={box}
                        />
                    </div>
                ))}
            </div>
        </div>
    );
}

export default withNamespaces()(BoxList);