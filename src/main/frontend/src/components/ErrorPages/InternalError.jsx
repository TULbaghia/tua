import {Link} from "react-router-dom";
import styles from "../../css/floatingbox.css"
import hamster from "../../images/xhamster_internal.png"
import {withNamespaces} from "react-i18next";

function InternalError(props) {
    const {t, i18n} = props

    return(
        <div className="floating-box">
            <img className="shadow p-3 mb-5 rounded" src={hamster} alt={t('hamster')}/>
            <h1>500</h1>
            <h1>{t('internal')}</h1>
            <Link to="/">
                {t('mainPage')}
            </Link>
        </div>
    )
}

export default withNamespaces()(InternalError);