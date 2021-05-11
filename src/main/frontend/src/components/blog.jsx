import React from "react";
import AuthService from "../AuthService";

function BlogScreen() {

    const handleShowToken = () => {
        console.log(AuthService.token)
    }

    return (
        <div>
            <h1>Blog</h1>
            <h4>Some blog post</h4>
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et
                dolore magna aliqua.</p>
            <h4>Another blog post</h4>
            <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
                consequat.</p>
            <p>Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
                pariatur.</p>
            <h4>Even more blog post</h4>
            <p>Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est
                laborum.</p>
            {/*TODO: Testing purpose*/}
            <button onClick={handleShowToken} className="btn btn-lg btn-primary btn-block">
                Show token
            </button>
        </div>
    );
}
export default BlogScreen;