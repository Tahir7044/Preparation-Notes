import {useState, useEffect} from 'react';
import './TrafficLight.css';



const traffic_light_config = [
    {
        color: "green",
        duration:5000
    },
    {
        color: "yellow",
        duration:2000
    },
    {
        color: "red",
        duration:3000
    }
]

const displayOrder = [
    {color:"green"},
    {color: "red"},
    {color:"yellow"}
]


const Light  = ({color}:{color:string})=>{

    return (
        <div className="light" style={{backgroundColor: color}}>
        </div>
    )

}


export const TrafficLight = ()=>{

    const [selectedColor, setSelectedColor] = useState<{color:string, duration:number} | null>(null);
    const [selectedIndex, setSelectedIndex] = useState(0);

    useEffect(()=>{
        const timer = setInterval(()=>{
            const getColor = traffic_light_config.filter(light=> light.color === displayOrder[selectedIndex].color)
            setSelectedColor(getColor[0]);
            setSelectedIndex((selectedIndex+1)%traffic_light_config.length)
        },selectedColor?.duration)
        return ()=> clearInterval(timer);
    },[selectedColor])

    return (
        <div className="traffic-light-container">
            <div className='light-conatiner'>
                {displayOrder.map(light=>(
                    <Light key={light.color} color={light.color===selectedColor?.color? light.color : ""} />
                ))}
            </div>
        </div>
    )
}