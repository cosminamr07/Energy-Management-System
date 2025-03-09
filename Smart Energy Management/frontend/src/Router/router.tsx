import { RouteObject, createBrowserRouter } from "react-router-dom";
import  SignIn from "../Pages/SignIn";
import  Register from "../Pages/Register";
import Platform from "../Pages/Platform";
import AdminPage from "../Pages/AdminPage";
import Simulator from "../Pages/Simulator";
import ProtectedRoute from "../Pages/ProtectedRoute"; // Importă ProtectedRoute

const routes: RouteObject[] = [
    {
        path: "/",
        element: <SignIn />
    },
    {
        path: "/Home",
        element: <div>Prima componenta</div>
    },
    {
        path: "/Login",
        element: <SignIn />
    },
    {
        path: "/Simulator",
        element: <Simulator />
    },
    {
        path: "/Register",
        element: <Register />
    },
    // {
    //     path: "/Platform",
    //     element: <Platform/>
    // },
    // {
    //     path: "/AdminPage",
    //     element: <AdminPage/>
    // },
    {
        path: "/AdminPage",
        element: (
            <ProtectedRoute requiredRole="Admin">
                <AdminPage />
            </ProtectedRoute>
        )
    },
    {
        path: "/Platform",
        element: (
            <ProtectedRoute requiredRole="Client">
                <Platform />
            </ProtectedRoute>
        )
    },
  

];

export const router = createBrowserRouter(routes)