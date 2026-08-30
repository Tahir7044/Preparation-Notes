import { useRef, useState } from "react";
import "./TabList.css";

type Item = { name: string; id: number };

export const TabList = ({ tabs }: { tabs: Item[] }) => {
  const [activeTab, setActiveTab] = useState(tabs[0]?.id);
  const tabRefs = useRef<(HTMLButtonElement | null)[]>([]);

  const handleTab = (id: number) => setActiveTab(id);

  const handleKeyDown = (e: React.KeyboardEvent, index: number) => {
    let next = index;
    if (e.key === "ArrowRight") next = (index + 1) % tabs.length;
    else if (e.key === "ArrowLeft") next = (index - 1 + tabs.length) % tabs.length;
    else if (e.key === "Home") next = 0;
    else if (e.key === "End") next = tabs.length - 1;
    else return;
    tabRefs.current[next]?.focus();
  };

  return (
    <div className="tab-container">
      <div className="tab-list" role="tablist">
        {tabs.map((tab, index) => (
          <button
            key={tab.id}
            id={`tab-${tab.id}`}
            ref={el => { tabRefs.current[index] = el }}
            role="tab"
            aria-selected={activeTab === tab.id}
            aria-controls={`panel-${tab.id}`}
            tabIndex={activeTab === tab.id ? 0 : -1}
            className={`tab ${activeTab === tab.id ? "tab--active" : ""}`}
            onClick={() => handleTab(tab.id)}
            onKeyDown={(e) => handleKeyDown(e, index)}
          >
            {tab.name}
          </button>
        ))}
      </div>
      {tabs.map(tab => (
        <div
          key={tab.id}
          id={`panel-${tab.id}`}
          role="tabpanel"
          aria-labelledby={`tab-${tab.id}`}
          hidden={activeTab !== tab.id}
          className="tab-content"
        >
          {tab.name}
        </div>
      ))}
    </div>
  );
};
