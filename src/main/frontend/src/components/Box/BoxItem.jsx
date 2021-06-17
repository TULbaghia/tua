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
    const {box, onModify, onDelete, isManager} = props;

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
        <Card className={"text-center card-item"}>
            <Card.Img className={"p-3"} style={{borderBottom: "1px solid var(--dark)"}} variant="top"
                      src={getIcon(box.animalType)}/>
            <Card.Body className={"card-item-body"}>
                <Card.Title id={v4()}>{i18n.t('box.price')}: {box.price}</Card.Title>
                <Card.Text>
                    {box.description}
                </Card.Text>
                {isManager() ? (
                    <>
                        <Button id={v4()} onClick={() => onModify(box.id)}
                                variant="warning">{i18n.t('button.edit')}</Button>
                        <Button id={v4()} onClick={() => onDelete(box.id)} className={"card-button"}
                                variant="danger">{i18n.t('button.delete')}</Button>
                    </>
                ) : (<></>)}

            </Card.Body>
        </Card>
    );
}

export default BoxItem;