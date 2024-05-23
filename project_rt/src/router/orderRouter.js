import { Suspense, lazy, useEffect } from "react";
import { Navigate } from "react-router-dom";
import useCustomLogin from "../hooks/useCustomLogin"; // useCustomLogin 훅 가져오기

const orderRouter = () => {
    const Loading = <div>Loading</div>;
    const OrderPage = lazy(() => import("../pages/order/OrderPage"));
    const OrderListPage = lazy(() => import("../pages/order/OrderListPage"));
    const OrderSuccess = lazy(() => import("../pages/order/OrderSuccessPage"));
    const OrderFail = lazy(() => import("../pages/order/OrderFailPage"));

    const PrivateRoute = ({ element }) => {
        const { isLogin } = useCustomLogin();

        useEffect(() => {
            if (!isLogin) {
                alert("로그인 해야만 합니다.");
            }
        }, [isLogin]);

        if (!isLogin) {
            return <Navigate to="/member/login" />;
        }
        return element;
    };

    return [
        {
            path: "",
            element: <PrivateRoute element={<Suspense fallback={Loading}><OrderPage /></Suspense>} />
        },
        {
            path: "list",
            element: <PrivateRoute element={<Suspense fallback={Loading}><OrderListPage /></Suspense>} />
        },
        {
            path: "success",
            element: <PrivateRoute element={<Suspense fallback={Loading}><OrderSuccess /></Suspense>} />
        },
        {
            path: "fail",
            element: <PrivateRoute element={<Suspense fallback={Loading}><OrderFail /></Suspense>} />
        }
    ];
};

export default orderRouter;
