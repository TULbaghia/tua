import React, {useState} from "react";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import i18n from "../../i18n";
import {Col, Container, Row} from "react-bootstrap";
import {Form, Formik} from "formik";
import {AddBoxValidationSchema} from "./AddBoxFormComponents/ValidationSchema";
import FieldComponent from "./AddBoxFormComponents/FieldComponent";
import {withNamespaces} from "react-i18next";
import "./AddBoxFormWrapper.scss"
import SelectComponent from "./AddBoxFormComponents/SelectComponent";
import {useHistory} from "react-router";
import {useLocale} from "../LoginContext";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {addBox} from "./AddBoxApiUtil";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";

function AddBoxForm() {

    const animals = ["DOG", "CAT", "RODENT", "BIRD", "RABBIT", "LIZARD", "TURTLE"]
    const history = useHistory();
    const {token} = useLocale();
    const [animalTypes, setAnimalTypes] = useState(animals);

    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchDialog = useDialogPermanentChange();

    const handleAddBox = (values, setSubmitting) => {
        dispatchDialog({
            callbackOnSave: () => {
                addBox({values, token}).then(res => {
                    dispatchNotificationSuccess({message: i18n.t('addBox.success')});
                    history.goBack();
                }).catch(err => {
                    ResponseErrorHandler(err, dispatchNotificationDanger);
                });
            },
            callbackOnCancel: () => {
                setSubmitting(false)
            },
        });
        setSubmitting(false);
    }

    return (
        <div id="add-box-form" className={"mb-5 container-fluid"}>
            <BreadCrumb>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('mainPage')}</Link>
                </li>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('managerDashboard')}</Link>
                </li>
                <li className="breadcrumb-item active">
                    <Link to="/boxes/own">{i18n.t('boxList.navbar.title')}</Link>
                </li>
                <li className="breadcrumb-item active" aria-current="page">
                    {i18n.t('addBox.title')}
                </li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={8} md={7} lg={6} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div>
                            <Row className="text-center justify-content-center d-block">
                                <h1 className="mb-3">{i18n.t('addBox.title')}</h1>
                                <p className="obligatory-fields mb-3">{i18n.t('obligatoryFields')}</p>
                            </Row>
                            <Formik initialValues={{
                                price: "",
                                description: "",
                                animalType: animals[0],
                            }}
                                    enableReinitialize
                                    validate={AddBoxValidationSchema}
                                    onSubmit={(values, {setSubmitting}) => handleAddBox(values, setSubmitting)}>
                                {({isSubmitting, handleChange}) => (
                                    <Form>
                                        <Row>
                                            <Col>
                                                <SelectComponent name="animalType"
                                                                 entryValue={animalTypes[0]}
                                                                 label={i18n.t('animalType')}
                                                                 values={animalTypes}
                                                                 handleChange={handleChange}/>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col>
                                                <FieldComponent name="price"
                                                                label={i18n.t('price') + " (" + i18n.t('currency') + ")"}
                                                                placeholder={i18n.t('price')}
                                                                handleChange={handleChange}/>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col>
                                                <FieldComponent name="description"
                                                                label={i18n.t('box.description')}
                                                                placeholder={i18n.t('box.description')}
                                                                handleChange={handleChange}/>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <button className="btn-background-custom btn btn-lg btn-primary mt-3"
                                                    type="submit"
                                                    disabled={isSubmitting}>
                                                {i18n.t('add')}
                                            </button>
                                        </Row>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default withNamespaces()(AddBoxForm)