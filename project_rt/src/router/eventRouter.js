import { Suspense, lazy, useEffect } from "react";
import { Navigate } from "react-router-dom";
import useCustomLogin from "../hooks/useCustomLogin"; // useCustomLogin 훅 가져오기

const Loading = <div>Loading....</div>;
const EventList = lazy(() => import("../pages/event/ListPage"));
const EventAdd = lazy(() => import("../pages/event/AddPage"));
const EventRead = lazy(() => import("../pages/event/ReadPage"));
const EventModify = lazy(() => import("../pages/event/ModifyPage"));

const eventRouter = () => {

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
      element: <Suspense fallback={Loading}><EventList /></Suspense>
    },
    {
      path: "",
      element: <Navigate replace to="/event/list" />
    },
    {
      path: "add",
      element: <PrivateRoute element={<Suspense fallback={Loading}><EventAdd /></Suspense>} />
    },
    {
      path: "read/:eno",
      element: <Suspense fallback={Loading}><EventRead /></Suspense>
    },
    {
      path: "modify/:eno",
      element: <PrivateRoute element={<Suspense fallback={Loading}><EventModify /></Suspense>} />
    }
  ];
};

export default eventRouter;
