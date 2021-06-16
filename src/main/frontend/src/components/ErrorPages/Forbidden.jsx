import {Link} from "react-router-dom";
import styles from "../../css/floatingbox.css"
import cat from "../../images/cat_forbidden.png"
import {withNamespaces} from "react-i18next";
import {useEffect} from "react";

function Forbidden(props) {
    const {t, i18n} = props

    useEffect(() => {
        document.title = t('animalHotel');
    }, [])

    return(
        <div className="floating-box">
            <img className="shadow p-3 mb-5 rounded" src={cat} alt={t('staringCat')}/>
            <h1>403</h1>
            <h1>{t('forbidden')}</h1>
            <Link to="/">
                {t('mainPage')}
            </Link>
        </div>
    )
}

export default withNamespaces()(Forbidden);