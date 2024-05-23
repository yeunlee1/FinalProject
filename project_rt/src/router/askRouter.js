import { Suspense, lazy } from "react";
import { Navigate } from "react-router-dom";

const askRouter = () => {

    const Loading = <div>Loading....</div>
    const AskList = lazy(()=>import("../pages/ask/ListPage"))
    const AskAdd = lazy(()=>import("../pages/ask/AddPage"))
    const AskRead = lazy(()=>import("../pages/ask/ReadPage"))
    const AskModify = lazy(()=>import("../pages/ask/ModifyPage"))

    return [
        {
            path: "",
            element: <Navigate replace to="/ask/list" />
        }, /* 주소창에 아무것도 안썻을 시 list로 리플레이스 */
        {
            path: "list",
            element: <Suspense fallback={Loading}><AskList /></Suspense>
        }, /* Suspense :  컴포넌트 렌더링 하는 동안 로딩상태 표시 */
        {
            path: "add",
            element: <Suspense fallback={Loading}><AskAdd /></Suspense>
        },
        {
            path: "read/:ano",
            element: <Suspense fallback={Loading}><AskRead /></Suspense>
        },
        {
            path: "modify/:ano",
            element: <Suspense fallback={Loading}><AskModify /></Suspense>
        }

    ]
}

export default askRouter;