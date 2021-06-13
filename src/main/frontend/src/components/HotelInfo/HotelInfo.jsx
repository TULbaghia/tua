import React from "react";
import cat from "../../images/cat.png"
import {withNamespaces} from "react-i18next";
import queryString from "query-string";
import "../../css/floatingbox.css";
import {useLocation} from "react-router";
import {ListGroup, Tab, Tabs} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import RatingComponent from "./RatingComponent";
import {faCity, faMapMarkedAlt} from '@fortawesome/free-solid-svg-icons'
import {Rating} from "@material-ui/lab";


function Home(props) {
    const {t, i18n} = props
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);

    return (
        <>
            <div id={"hotelInfo"} className={"container mt-2 p-4 mb-5"}>
                <div className={"row"}>
                    <div className={"col-md-6 col-sm-8 col-10 mb-2"}>
                        <h2>Nazwa hotelu</h2>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-4 col-sm-6 col-8 mb-3"}>
                        <img alt="cat" className="img-fluid" src={cat}/>
                    </div>
                    <div className={"col-md-8 col-sm-6 col-4"}>
                        <Tabs defaultActiveKey="description" transition={false} id="tab">
                            <Tab eventKey="description" title={t('description')}>
                                <div className={"text-justify mt-2"}>Lorem ipsum dolor sit amet, consectetur adipiscing
                                    elit. Vestibulum
                                    aliquam nisl id tempor fermentum. Aliquam egestas tincidunt orci ut tincidunt. Quisque ac
                                    placerat dui. In sed tellus vel lectus ultricies ornare a in mi. Curabitur gravida arcu sit
                                    amet fringilla iaculis. Nulla odio ante, iaculis non feugiat lacinia, varius ac arcu. Sed
                                    viverra luctus erat. Sed semper erat libero. Ut eget aliquet risus. Nunc nisi sem, egestas eu
                                    convallis sed, viverra vel lacus. Donec ut neque purus. Integer convallis lacinia nisi. Duis
                                    condimentum diam justo, eget rhoncus sapien pretium non. Donec leo ligula, suscipit et finibus
                                    id, eleifend sed est. Aliquam varius facilisis ex, ut pharetra massa. Donec elementum gravida
                                    sagittis.
                                </div>
                            </Tab>
                            <Tab eventKey="location" title={t('location')}>
                                <ListGroup variant={"flush"}>
                                    <ListGroup.Item className={"d-flex align-items-center"}>
                                        <FontAwesomeIcon icon={faCity} size={"2x"}/>
                                        <span className={"ml-3"}>Miasto</span>
                                    </ListGroup.Item>
                                    <ListGroup.Item className={"d-flex align-items-center"}>
                                        <FontAwesomeIcon icon={faMapMarkedAlt} size={"2x"}/>
                                        <span className={"ml-3"}>Ulica</span>
                                    </ListGroup.Item>
                                </ListGroup>
                            </Tab>
                        </Tabs>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-4 d-flex align-items-center"}>
                        <h4 className={"m-0"}>Komentarze</h4>
                    </div>
                    <div className={"col-md-8 d-flex align-items-center justify-content-end"}>
                        <span className={"mr-3"}>Średnia ocen:</span>
                        <Rating
                            size={"large"}
                            defaultValue={1.6}
                            readOnly
                            precision={0.5}
                        />
                        <span className={"ml-3"}>[1.6]</span>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-md-12 mt-3"}>
                        <RatingComponent rate={5} login={"Tomek666"}
                                         content={"It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English."}
                                         date={"15-02-2021"}/>
                        <RatingComponent rate={3.5} login={"Adam222"}
                                         content={"It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English."}
                                         date={"20-03-2020"}/>
                        <RatingComponent rate={4} login={"Albert997"}
                                         content={"It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English."}
                                         date={"11-08-2021"}/>
                    </div>
                </div>
            </div>
        </>
    );
}

export default withNamespaces()(Home);