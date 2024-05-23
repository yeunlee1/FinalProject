import { Suspense, lazy, useEffect } from "react";
import { Navigate } from "react-router-dom";
import useCustomLogin from "../hooks/useCustomLogin"; // useCustomLogin 훅 가져오기

const productRouter = () => {
    const Loading = <div>Loading</div>;
    const ProductsList = lazy(() => import("../pages/products/ListPage"));
    const ProductsSetList = lazy(() => import("../pages/products/SetListPage"));
    const ProductsOneList = lazy(() => import("../pages/products/OneListPage"));
    const ProductsAdd = lazy(() => import("../pages/products/AddPage"));
    const ProductRead = lazy(() => import("../pages/products/ReadPage"));
    const ProductModify = lazy(() => import("../pages/products/ModifyPage"));

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
            element: <Suspense fallback={Loading}><ProductsList /></Suspense>
        },
        {
            path: "setlist",
            element: <Suspense fallback={Loading}><ProductsSetList /></Suspense>
        },
        {
            path: "onelist",
            element: <Suspense fallback={Loading}><ProductsOneList /></Suspense>
        },
        {
            path: "",
            element: <Navigate replace to="/products/list" />
        },
        {
            path: "add",
            element: <PrivateRoute element={<Suspense fallback={Loading}><ProductsAdd /></Suspense>} />
        },
        {
            path: "read/:pno",
            element: <Suspense fallback={Loading}><ProductRead /></Suspense>
        },
        {
            path: "modify/:pno",
            element: <PrivateRoute element={<Suspense fallback={Loading}><ProductModify /></Suspense>} />
        }
    ];
};

export default productRouter;
