import React from "react";
import {useHistory} from "react-router";
import {Link, useParams} from "react-router-dom";
import {withNamespaces} from 'react-i18next';
import BreadCrumb from "../Partial/BreadCrumb";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import i18n from "../../i18n";
import {validatorFactory, ValidatorType} from "../Validation/Validators";
import {dispatchErrors, ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {Col, Container, Row} from "react-bootstrap";
import {api} from "../../Api";

function EmailConfirm() {
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchCriticalDialog = useDialogPermanentChange();
    const history = useHistory();
    let {code} = useParams();

    const handleConfirmation = () => (
        dispatchCriticalDialog({
            callbackOnSave: () => handleSubmit(),
        })
    )

    const handleSubmit = () => {
        if (validatorFactory(code, ValidatorType.PEN_CODE)) {
            api.confirmEmail(code).then((res) => {
                dispatchNotificationSuccess({
                    message: i18n.t('emailConfirm.success.info')
                });
                history.push("/login");
            }).catch(err => {
                ResponseErrorHandler(err, dispatchNotificationDanger, false, (error) => {
                    dispatchErrors(error, dispatchNotificationDanger);
                });
            })
        }
    }

    return (
        <div className={"container-fluid"}>
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('emailConfirm.title')}</li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={10} md={7} lg={6} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div className="">
                            <form className="form-signin p-0">
                                <h3 className="mb-4">{i18n.t('emailConfirm.title')}</h3>
                                <span>{i18n.t('emailConfirm.info')}</span>
                                <button className="btn btn-lg btn-primary btn-block mt-5"
                                        type="button"
                                        onClick={() => handleConfirmation()}
                                        style={{backgroundColor: "#7749F8", whiteSpace: 'normal'}}>
                                    {i18n.t('emailConfirm.action')}
                                </button>
                            </form>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(EmailConfirm);
