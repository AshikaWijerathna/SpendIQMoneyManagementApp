import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import Home from "./pages/Home";
import Income from "./pages/Income";
import Expense from "./pages/Expense";
import Category from "./pages/Category";
import Filter from "./pages/Filter";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Budget from "./pages/Budget";
import Goals from "./pages/Goals.jsx";
import Reports from "./pages/Reports.jsx";
import Bills from "./pages/Bills.jsx";

const App = () => {
    return(
       <>
       <Toaster />  

        <BrowserRouter>
        <Routes>
            <Route path="/" element={<Root />} />
            <Route path="/dashboard" element={<Home />}/>    
            <Route path="/income" element={<Income />}/>    
            <Route path="/expense" element={<Expense />}/>    
            <Route path="/category" element={<Category />}/>    
            <Route path="/filter" element={<Filter />}/>    
            <Route path="/login" element={<Login />}/>    
            <Route path="/signup" element={<Signup />}/> 
            <Route path="/budget" element={<Budget />}/> 
            <Route path="/goals" element={<Goals />} />
            <Route path="/reports" element={<Reports />} />
            <Route path="/bills" element={<Bills />} />
        </Routes> 
        </BrowserRouter> 
       </>
    )
}

const Root = () =>{
    const isAuthenticated = !!localStorage.getItem("token");
    return isAuthenticated ? (
        <Navigate to="/dashboard" />
    ): (
        <Navigate to="/login" />
    );
}

export default App;