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
    return <div className="light" style={{backgroundColor: color}} />
}


export const TrafficLight = ()=>{

    const [selectedColor, setSelectedColor] = useState<{color:string, duration:number} | null>(null);
    // const [selectedIndex, setSelectedIndex] = useState(0);

    // useEffect(()=>{
    //     const timer = setTimeout(()=>{
    //         const getColor = traffic_light_config.filter(light=> light.color === displayOrder[selectedIndex].color)
    //         setSelectedColor(getColor[0]);
    //         setSelectedIndex((selectedIndex+1)%traffic_light_config.length)
    //     },selectedColor?.duration)
    //     return ()=> clearTimeout(timer);
    // },[selectedColor])

    useEffect(()=>{
        let timer: number;
        let index = 0;
        const tick = ()=>{
            const getColor = traffic_light_config.filter(light => light.color === displayOrder[index].color)
            setSelectedColor(getColor[0]);
            index = (index+1)%traffic_light_config.length;
            timer = setTimeout(tick, getColor[0]?.duration)
        }
        timer = setTimeout(tick, 0);
        return () => clearTimeout(timer);

    },[])

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
