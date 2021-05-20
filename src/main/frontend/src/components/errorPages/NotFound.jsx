import {Link} from "react-router-dom";
import styles from "../../css/floatingbox.css"
import cat from "../../images/cat_not_found.png"
import {withNamespaces} from "react-i18next";

function NotFound(props) {
    const {t, i18n} = props
    return(
        <div className="floating-box">
            <img className="shadow p-3 mb-5 rounded" src={cat} alt={t('cryingCat')}/>
            <h1>404</h1>
            <h1>{t('pageNotFound')}</h1>
            <Link to="/">
                {t('mainPage')}
            </Link>
        </div>
    )
}

export default withNamespaces()(NotFound);