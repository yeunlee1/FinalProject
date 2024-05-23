import { Suspense, lazy, useEffect } from "react";
import { Navigate } from "react-router-dom";
import useCustomLogin from "../hooks/useCustomLogin"; // useCustomLogin 훅 가져오기

const Loading = <div>Loading....</div>;
const NoticeList = lazy(() => import("../pages/notice/ListPage"));
const NoticeAdd = lazy(() => import("../pages/notice/AddPage"));
const NoticeRead = lazy(() => import("../pages/notice/ReadPage"));
const NoticeModify = lazy(() => import("../pages/notice/ModifyPage"));

const noticeRouter = () => {

  const PrivateRoute = ({ element }) => {
    const { isLogin, isAdmin } = useCustomLogin();

    useEffect(() => {
      if (!isLogin) {
        alert("로그인 해야만 합니다.");
      } else if (!isAdmin()) {
        alert("해당 메뉴를 사용할 수 있는 권한이 없습니다.");
      }
    }, [isLogin, isAdmin]);

    if (!isLogin) {
      return <Navigate to="/member/login" />;
    }
    if (!isAdmin()) {
      return <Navigate to="/" />; // 메인 페이지로 리다이렉트
    }
    return element;
  };

  return [
    {
      path: "list",
      element: <Suspense fallback={Loading}><NoticeList /></Suspense>
    },
    {
      path: "",
      element: <Navigate replace to="/notice/list" />
    },
    {
      path: "add",
      element: <PrivateRoute element={<Suspense fallback={Loading}><NoticeAdd /></Suspense>} />
    },
    {
      path: "read/:nno",
      element: <Suspense fallback={Loading}><NoticeRead /></Suspense>
    },
    {
      path: "modify/:nno",
      element: <PrivateRoute element={<Suspense fallback={Loading}><NoticeModify /></Suspense>} />
    }
  ];
};

export default noticeRouter;
