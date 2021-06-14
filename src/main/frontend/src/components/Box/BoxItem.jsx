import React from "react";
import {Button, Card} from "react-bootstrap";
import Cat from "../../images/animals/cat.png"
import Dog from "../../images/animals/dog.png"
import Lizard from "../../images/animals/lizard.png"
import Mouse from "../../images/animals/mouse.png"
import Parrot from "../../images/animals/parrot.png"
import Rabbit from "../../images/animals/rabbit.png"
import Turtle from "../../images/animals/turtle.png"
import i18n from '../../i18n';
import {v4} from "uuid";

function BoxItem(props) {
    const {box, onModify, onDelete} = props;

    const getIcon = (animalType) => {
        switch (animalType) {
            case 'DOG':
                return Dog;
            case 'CAT':
                return Cat;
            case 'RODENT':
                return Mouse;
            case 'BIRD':
                return Parrot;
            case 'RABBIT':
                return Rabbit;
            case 'LIZARD':
                return Lizard;
            case 'TURTLE':
                return Turtle;
        }
    }

    return (
        <Card className={"text-center min-vw-75 card-item"}>
            <Card.Img className={"p-3"} style={{borderBottom: "1px solid var(--dark)"}} variant="top"
                      src={getIcon(box.animalType)}/>
            <Card.Body>
                <Card.Title id={v4()}>{i18n.t('box.price')}: {box.price}</Card.Title>
                <Card.Text>
                    {box.animalType}
                </Card.Text>
                <Button id={v4()} onClick={() => onModify(box.id)} variant="warning">{i18n.t('button.edit')}</Button>
                <Button id={v4()} onClick={() => onDelete(box.id)} className={"card-button"}
                        variant="danger">{i18n.t('button.delete')}</Button>
            </Card.Body>
        </Card>

        //   /////////////////////////////////
        //   <div className="card text-center my-2">
        //       <div className="card-body">
        //           <h5 className="card-title">
        //               {box.animalType} {box.hotelId}
        //           </h5>
        //           <h6 className="card-subtitle mb-2 text-muted">{box.price}</h6>
        //           <p className="card-text">ID: {box.id}</p>
        //           {/* <button className="btn btn-secondary btn-sm">Click</button> */}
        //           <button
        //               className="btn btn-warning btn-sm"
        //               onClick={() => onModify(box.id)}
        //           >
        //               Modify
        //           </button>
        //           {/* <button
        //   className="btn btn-danger btn-sm "
        //   onClick={() => onDelete(user.uuid)}
        // >
        //   Delete
        // </button> */}
        //       </div>
        //   </div>
        //   //////////////////////////////////
    );
}

export default BoxItem;