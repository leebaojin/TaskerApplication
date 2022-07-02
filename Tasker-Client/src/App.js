import logo from './logo.svg';
import './App.css';
import Home from './components/Home';
import {Router} from 'react-router-dom';

function App() {

  

  return (
    <div className="App">
      <Home/>
      
    </div>
  );
}

export default App;
//<Router exact to="/" component={Home}></Router>