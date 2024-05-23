import { Navigate, createSearchParams, useNavigate } from "react-router-dom";
import { useRecoilState, useResetRecoilState } from "recoil";
import { loginPost } from "../api/memberApi";
import { removeCookie, setCookie } from "../util/cookieUtil";
import signinState from "../atoms/signinState";
import { cartState } from "../atoms/cartState";

const useCustomLogin = () => {
  const navigate = useNavigate();

  const [loginState, setLoginState] = useRecoilState(signinState);
  const resetState = useResetRecoilState(signinState);
  const resetCartState = useResetRecoilState(cartState);

  const isLogin = loginState.email ? true : false;

  const doLogin = async (loginParam) => {
    const result = await loginPost(loginParam);

    console.log(result);

    saveAsCookie(result);

    return result;
  };

  const saveAsCookie = (data) => {
    setCookie("member", JSON.stringify(data), 1); // 1일
    setLoginState(data);
  };

  const doLogout = () => {
    removeCookie("member");
    resetState();
    resetCartState();
  };

  const exceptionHandle = (ex) => {
    console.log("Exception------------------------");
    console.log(ex);

    const errorMsg = ex.response.data.error;
    const errorStr = createSearchParams({ error: errorMsg }).toString();

    if (errorMsg === "REQUIRE_LOGIN") {
      alert("로그인 해야만 합니다.");
      navigate({ pathname: "/member/login", search: errorStr });

      return;
    }

    if (ex.response.data.error === "ERROR_ACCESSDENIED") {
      alert("해당 메뉴를 사용할 수 있는 권한이 없습니다.");
      navigate({ pathname: "/member/login", search: errorStr });
      return;
    }
  };

  const moveToPath = (path) => {
    navigate({ pathname: path }, { replace: true });
  };

  const moveToLogin = () => {
    navigate({ pathname: "/member/login" }, { replace: true });
  };

  const moveToLoginReturn = () => {
    return <Navigate replace to="/member/login" />;
  };

  const isAdmin = () => {
    // 관리자 또는 매니저 권한 확인
    return loginState.roleNames.includes("ADMIN") || loginState.roleNames.includes("MANAGER");
  };

  return {
    loginState,
    isLogin,
    doLogin,
    doLogout,
    moveToPath,
    moveToLogin,
    moveToLoginReturn,
    exceptionHandle,
    saveAsCookie,
    isAdmin, // 관리자 여부 확인 함수 반환 객체에 추가
  };
};

export default useCustomLogin;
