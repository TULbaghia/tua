import {useFormikContext, Field} from "formik"

export default function GridItemInput({name, placeholder, type}){
    const formik = useFormikContext();

    const errors = formik.errors[name]
    const touched = formik.touched[name]

    return (
        <div className="col-md-6">
            <Field 
                required={true} 
                placeholder={placeholder} 
                name={name} 
                type={type} 
                className="form-control mt-3" 
                style={{marginBottom: "1rem", width: "90%", display: "inline-block"}}/>
            <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
            {errors && touched && errors.map((err, i) => {
                return (<div style={{color: "red"}}>{err}</div>)
            })}
        </div>
    )
}