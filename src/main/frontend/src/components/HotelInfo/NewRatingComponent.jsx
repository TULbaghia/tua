import {Field, Form, Formik} from "formik";
import React from "react";
import {DropdownButton, Dropdown, Button, Container, Row} from "react-bootstrap";
import i18n from "i18next";
import {api} from "../../Api";
import {Rating} from "@material-ui/lab";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {NewRatingValidator} from "./NewRatingValidator";
import {useLocale} from "../LoginContext";

export default function NewRatingComponent({placeholder, header, buttonText, bookings, triggerRefresh, singleItemButton}) {
    const dangerNotifier = useNotificationDangerAndInfinity();
    const successNotifier = useNotificationSuccessAndShort();
    const confirmDialog = useDialogPermanentChange();
    const {token} = useLocale();

    const addNewRating = (newRate) => {
        api.addRating(newRate, {
            headers: {
                Authorization: token,
            }
        }).then(() => {
            successNotifier({
                message: i18n.t("comment.success.create")
            })
            triggerRefresh()
        }).catch((e) => ResponseErrorHandler(e, dangerNotifier));
    }

    const createRateDialog = (newRate) => {
        confirmDialog({
            callbackOnSave: (e) => addNewRating(newRate)
        })
    }

    const handleButton = (e, item, values) => {
        let newRate = {...values, bookingId: item.id}
        if (newRate.comment === "") {
            delete newRate.comment;
        }
        console.log(newRate);
        createRateDialog(newRate);
    }

    return (
        <div>
            <h4 className={"m-0"}>{header}</h4>
            <Formik initialValues={{
                comment: "",
                rate: 0,
            }}
                    initialErrors={{
                        dummy: ""
                    }}
                    enableReinitialize
                    validate={NewRatingValidator}
                    onSubmit={(values, {setSubmitting}) => console.log("submitvalues:" + values)}>
                {({isSubmitting, handleChange, errors, values}) => (
                    <Form>
                        <Field className="text-area-custom w-100 my-2 form-control"
                               as="textarea"
                               name="comment"
                               placeholder={placeholder}
                               onChange={handleChange}
                        >
                        </Field>

                        <Container>
                            <Row>
                                <Rating
                                    size="large"
                                    name="rate"
                                    defaultValue={0}
                                    autoSave={true}
                                    precision={1}
                                    onChange={handleChange}
                                />
                            </Row>
                            <Row>
                                {!singleItemButton ?
                                    <DropdownButton title={buttonText}
                                                    disabled={bookings.length < 1 || isSubmitting || Object.keys(errors).length > 0}>
                                        {
                                            bookings.map(item => (
                                                <Dropdown.Item disabled={isSubmitting} className={"dark-text-hover"} onClick={e => handleButton(e, item, values)}>{i18n.t('booking') + " id: " + item.id + ", price: " + item.price}</Dropdown.Item>
                                            ))
                                        }
                                    </DropdownButton>
                                    :
                                    <Button className="" disabled={bookings.length < 1 || isSubmitting || Object.keys(errors).length > 0} onClick={e => handleButton(e, bookings[0], values)}>
                                        {buttonText}
                                    </Button>
                                }
                            </Row>
                        </Container>
                    </Form>
                )}
            </Formik>
        </div>
    )
}