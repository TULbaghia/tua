import React, { useState } from "react";
import { useHistory } from "react-router";
import { withNamespaces } from "react-i18next";
import BreadCrumb from "./../Partial/BreadCrumb";
import { Link } from "react-router-dom";
import { api } from "../../Api";
import {
  useNotificationDangerAndInfinity,
  useNotificationSuccessAndShort,
  useNotificationWarningAndLong,
} from "./../Utils/Notification/NotificationProvider";
import { ResponseErrorHandler } from "./../Validation/ResponseErrorHandler";
import { validatorFactory, ValidatorType } from "./../Validation/Validators";
import { useThemeColor } from "./../Utils/ThemeColor/ThemeColorProvider";
import { Form, Formik, FieldArray } from "formik";
import SelectField from "./../controls/SelectField";
import "../../css/overlay.css";
import "react-datepicker/dist/react-datepicker.css";
import { Field } from "formik";
import DatePickerField from "../controls/DatePickerField";
import "../../css/booking-form.css";
import { useLocale } from "../LoginContext";

function SignUp(props) {
  const { t, i18n } = props;
  const dispatchNotificationDanger = useNotificationDangerAndInfinity();
  const dispatchNotificationWarning = useNotificationWarningAndLong();
  const dispatchNotificationSuccess = useNotificationSuccessAndShort();
  const history = useHistory();
  const [submitting, setSubmitting] = useState(false);
  const { token, setToken } = useLocale();

  const colorTheme = useThemeColor();

  // todo remove
  const hotels = [
    {
      label: "hotel_1", // name
      value: -1, // id
    },
    {
      label: "hotel_2",
      value: -2,
    },
    {
      label: "hotel_3",
      value: -3,
    },
    {
      label: "hotel_4",
      value: -4,
    },
  ];

  const animalTypes = [
    {
      label: "dog",
      value: 0,
    },
    {
      label: "cat",
      value: 1,
    },
    {
      label: "rodent",
      value: 2,
    },
    {
      label: "bird",
      value: 3,
    },
    {
      label: "rabbit",
      value: 4,
    },
    {
      label: "lizard",
      value: 5,
    },
    {
      label: "turtle",
      value: 6,
    },
  ];

  const initialValues = {
    hotelId: -1,
    dateFrom: new Date(),
    dateTo: new Date(),
    boxes: [
      {
        type: 0,
        quantity: 1,
      },
    ],
  };

  function onSubmit(values, { resetForm }) {
    setSubmitting(true);
    console.log(values);

    const { ...dto } = values;
    api
      .addBooking(dto, { headers: { Authorization: token } })
      .then((res) => {
        dispatchNotificationSuccess({
          message: t("booking.create.success"),
        });
        resetForm();
      })
      .catch((err) => {
        ResponseErrorHandler(err, dispatchNotificationDanger);
      })
      .finally(() => {
        setSubmitting(false);
      });
  }

  function validate(values) {
    const errors = {};
    if (values.dateFrom >= values.dateTo) {
      errors.dateFrom = t("booking.form.error.date_not_earlier");
    }
    return errors;
  }

  return (
    <Formik {...{ initialValues, validate, onSubmit, submitting }}>
      {({ values, errors, touched }) => (
        <div className="container">
          <BreadCrumb>
            <li className="breadcrumb-item">
              <Link to="/">{t("mainPage")}</Link>
            </li>
            <li className="breadcrumb-item active" aria-current="page">
              {t("booking.form.bread_crumb")}
            </li>
          </BreadCrumb>
          <div className="floating-box pt-2 pb-2">
            <h3 className="h3 text-center mt-3">{t("booking.form.title")}</h3>

            <Form className="row g-3">
              <div
                className="overlay"
                style={{ display: submitting ? "inline-flex" : "none" }}
              >
                <div class="spinner-border" role="status">
                  <span class="sr-only">Loading...</span>
                </div>
              </div>

              <div className="col-md-12 mb-2">
                <label htmlFor="hotelId">{t('hotel')}</label>
                <Field
                  className="col-md-5"
                  name="hotelId"
                  component={SelectField}
                  options={hotels}
                />
              </div>

              <div className="col-md-12">
                <label htmlFor="dateFrom">{t('booking.form.date_from')}</label>
                {errors && touched && errors.dateFrom && (
                  <div style={{ color: "red" }}>{errors.dateFrom}</div>
                )}
                <DatePickerField name="dateFrom" />
              </div>

              <div className="col-md-12">
                <label htmlFor="dateTo">{t('booking.form.date_to')}</label>
                <DatePickerField name="dateTo" />
              </div>

              <div className="col-md-12">
                <label htmlFor="boxes">{t('booking.form.boxes')}</label>
                <FieldArray name="boxes">
                  {({ push }) => {
                    const len = values.boxes.length;
                    return (
                      <div className="col-md-12">
                        {values.boxes.map((box, index) => (
                          <div key={index} className="row my-2">
                            <div className="col-md-5">
                              <Field
                                className="col-md-5"
                                name={`boxes.${index}.type`}
                                component={SelectField}
                                options={animalTypes}
                              />
                            </div>
                            <Field
                              className="col-md-5"
                              name={`boxes.${index}.quantity`}
                              type="number"
                              min="1"
                            />
                            {len === index + 1 && (
                              <div className="col-md-2">
                                <button
                                  type="button"
                                  className="btn btn-primary"
                                  style={{ backgroundColor: "#7749F8" }}
                                  onClick={() =>
                                    push({ type: "", quantity: "" })
                                  }
                                >
                                  +
                                </button>
                              </div>
                            )}
                          </div>
                        ))}
                      </div>
                    );
                  }}
                </FieldArray>
              </div>

              <div className="col-12 d-flex justify-content-center mb-3">
                <button
                  className="btn btn-primary"
                  style={{ backgroundColor: "#7749F8" }}
                  type="submit"
                >
                  {t("send")}
                </button>
              </div>
            </Form>
          </div>
        </div>
      )}
    </Formik>
  );
}

export default withNamespaces()(SignUp);
