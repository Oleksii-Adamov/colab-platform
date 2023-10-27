import TestPage from "@/pages/TestPage";
import LandingPage from "@/pages/LandingPage";


export default [
    {
        path: '/',
        component: LandingPage
    },
    {
        path: '/test',
        component: TestPage
    },
    {
        path: '/:unused*',
        redirect: '/',
    }
]