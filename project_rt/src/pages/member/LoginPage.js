import BasicMenu from "../../layouts/BasicMenu";
import LoginComponent from './../../components/member/LoginComponent';



const LoginPage = () => {
    return (
        <div className='top-0 left-0 z-[1055] flex flex-col h-full w-full'>
            <div className="w-full flex flex-wrap  h-full justify-center  items-center ">
                <LoginComponent />
            </div>
        </div>
    );
}

export default LoginPage;