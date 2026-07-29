import logo from "./logo.png";
import login_bg from "./login_bg.png";
import { BarChart3, CalendarClock, Coins, FunnelPlus, LayoutDashboard, List, Target, Wallet, WalletCards } from "lucide-react";


export const assets = {
    logo,
    login_bg,
}

export const SIDE_BAR_DATA = [
    {
        id: "01",
        label: "Dashboard",
        icon: LayoutDashboard,
        path: "/dashboard",
    },
    {
        id:"02",
        label: "Category",
        icon:List,
        path: "/category",
    },
    {
        id:"03",
        label: "Income",
        icon: Wallet,
        path: "/income",
    },
    {
        id:"04",
        label:"Expense",
        icon: Coins,
        path: "/expense",
    },
    {
        id: "05",
        label: "Filters",
        icon: FunnelPlus,
        path: "/filter",
    },
    {
        id:"06",
        label: "Budget",
        icon: WalletCards,
        path:"/budget",
    },
    {
        id:"07",
        label:"Goals",
        icon: Target,
        path:"/goals",
    },
    {
        id:"08",
        label:"Reports",
        icon: BarChart3,
        path:"/reports",
    },
    {
        id:"09",
        label:"Bills",
        icon: CalendarClock,
        path:"/bills",
    }
]