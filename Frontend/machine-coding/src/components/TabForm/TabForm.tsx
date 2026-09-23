import React, {useState} from 'react';
import {Interest} from './Interest';
import {Profile} from './Profile';
import {Setting} from './Setting';
import './TabForm.css';



export const TabForm = ()=>{

    const [activeTab, setActiveTab] = useState(0);
    const [errors, setErrors] = useState({});
    const [state, setState] = useState({});

    const config = [
        {
            name: "Profile",
            Component:Profile,
            validate: (state)=>{
                const {name, age, email} = state;
                let isError = false;
                let err = {};
                if(!name || name.length<3){
                    err.name = "name should be geater than 2 character";
                    isError = true;
                }

                if(!age || parseInt(age, 10)<18 ||  parseInt(age, 10)>100){
                    err.age = "age should be between 18 to 100";
                    isError = true;
                }

                if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                    err.email = "enter a valid email";
                    isError = true;
                }
                setErrors(err);
                return isError;
            }
        },
        {
            name: "Interest",
            Component:Interest,
            validate: (state)=>{
                const {interests} = state;
                let isError = false;
                let err = {};
                if(!interests || interests.length<1){
                    err.interests = "Select atleast one interest";
                    isError = true;
                }
                setErrors(err);
                return isError;
            }
        },
        {
            name: "Setting",
            Component:Setting,
            validate: (state)=>{
                const {settings} = state;
                let isError = false;
                let err = {};
                if(!settings || Object.entries(settings).length<1){
                    err.settings = "Select atleast one setting";
                    isError = true;
                }
                setErrors(err);
                return isError;
            }
        }
    ]

    const ActiveComponent = config[activeTab].Component;

    
    const handleSubmit = ()=>{
        if(config[activeTab].validate(state)){
            return;
        }
        console.log("subbmitted",state)
        
    }
    const handleNext = ()=>{
        if(config[activeTab].validate(state)){
            return;
        }
        setActiveTab(activeTab+1)
        
    }
    const handlePrev = ()=>{
        if(config[activeTab].validate(state)){
            return;
        }
        setActiveTab(activeTab-1)
        
    }

    return (
        <div className="tabform-container">
            <div>
                {config.map((tab, index)=>(
                    <button role="tab" className={`tab ${activeTab==index? 'tab--active': ''}`} key={tab.name} onClick={()=> setActiveTab(index)}>
                        {tab.name}
                    </button>
                ))}
            </div>

            <div role="tabpanel" className='tab-body'>
                <ActiveComponent state={state} setState={setState} errors={errors}/>
            </div>

            <div>

                {activeTab>0 && (
                    <button onClick={handlePrev}>
                        Pre
                    </button>
                )}

                {activeTab<config.length-1 && (
                    <button onClick={handleNext}>
                        Next
                    </button>
                )}

                {activeTab==config.length-1 && (
                    <button onClick={handleSubmit}>
                        Submit
                    </button>
                )}
            </div>

        </div>
    )


}