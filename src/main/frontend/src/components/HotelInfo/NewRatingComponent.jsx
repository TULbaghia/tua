import {Field, Form, Formik} from "formik";
import React from "react";
import {DropdownButton, Dropdown} from "react-bootstrap";
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

export default function NewRatingComponent({placeholder, header, buttonText, bookings, triggerRefresh}) {
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
                        <Rating
                            size="large"
                            name="rate"
                            defaultValue={0}
                            autoSave={true}
                            precision={1}
                            onChange={handleChange}
                        />
                        <DropdownButton title={buttonText}
                                        disabled={bookings.length < 1 || isSubmitting || Object.keys(errors).length > 0}>
                            {
                                bookings.map(item => (
                                    <Dropdown.Item disabled={isSubmitting} onClick={e => {
                                        let newRate = {...values, bookingId: item.id}
                                        if (newRate.comment === "") {
                                            delete newRate.comment;
                                        }
                                        console.log(newRate);
                                        createRateDialog(newRate);
                                    }}>{i18n.t('booking') + " id: " + item.id + ", price: " + item.price}</Dropdown.Item>
                                ))
                            }
                        </DropdownButton>
                    </Form>
                )}
            </Formik>
        </div>
    )
}