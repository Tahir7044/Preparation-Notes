import React, { useState, useRef } from 'react'
import './VirtualList.css'

const CONTAINER_HEIGHT = 400
const ROW_HEIGHT = 40
const BUFFER = 10

type Item = { name: string; id: number }

const throttle = (fn: Function, interval: number) => {
  let lastCall = 0;
  return (...args: any[]) => {
    const now = Date.now();
    if (now - lastCall >= interval) {
      lastCall = now;
      fn(...args);
    }
  };
}

const useVirtualScroll = (length: number) => {
  const [scrollTop, setScrollTop] = useState(0);
  const handleScroll = (e: React.UIEvent<HTMLDivElement>) => setScrollTop(e.currentTarget.scrollTop);
  const throttledScroll = useRef(throttle(handleScroll, 100)).current
  const startIndex = Math.max(0, Math.floor(scrollTop / ROW_HEIGHT) - BUFFER);
  const endIndex = Math.min(length - 1, Math.floor((scrollTop + CONTAINER_HEIGHT) / ROW_HEIGHT) + BUFFER);
  return { startIndex, endIndex, throttledScroll };
}

const SpacerDivList = ({ data }: { data: Item[] }) => {
  const { startIndex, endIndex, throttledScroll } = useVirtualScroll(data.length);
  return (
    <div style={{ height: `${CONTAINER_HEIGHT}px` }} className="container" onScroll={throttledScroll}>
      <div style={{ height: `${startIndex  * ROW_HEIGHT}px` }} />
      {data.slice(startIndex, endIndex + 1).map(item => (
        <div style={{ height: `${ROW_HEIGHT}px` }} className="item" key={item.id}>
          {item.name}
        </div>
      ))}
      <div style={{ height: `${(data.length - endIndex-1) * ROW_HEIGHT}px` }} />
    </div>
  )
}

const PaddingTop = ({ data }: { data: Item[] }) => {
  const { startIndex, endIndex, throttledScroll } = useVirtualScroll(data.length);
  return (
    <div style={{ height: `${CONTAINER_HEIGHT}px` }} className="container" onScroll={throttledScroll}>
      <div style={{ 
        minHeight: `${data.length  * ROW_HEIGHT}px`,
        paddingTop: `${startIndex*ROW_HEIGHT}px`}} >
      {data.slice(startIndex, endIndex + 1).map(item => (
        <div style={{ height: `${ROW_HEIGHT}px` }} className="item" key={item.id}>
          {item.name}
        </div>
      ))}
      </div>
    </div>
  )
}

const AbsolutePositionList = ({ data }: { data: Item[] }) => {
  const { startIndex, endIndex, throttledScroll } = useVirtualScroll(data.length);
  return (
    <div style={{ height: `${CONTAINER_HEIGHT}px` }} className="container" onScroll={throttledScroll}>
      <div style={{ height: `${data.length * ROW_HEIGHT}px`, position: 'relative' }}>
        {data.slice(startIndex, endIndex + 1).map(item => (
          <div className="item" style={{ position: 'absolute', height: `${ROW_HEIGHT}px`, top: `${item.id * ROW_HEIGHT}px` }} key={item.id}>
            {item.name}
          </div>
        ))}
      </div>
    </div>
  )
}

export const VirtualList = ({ data }: { data: Item[] }) => {
  return (
    <div style={{ display: 'flex', flexDirection: 'row', gap: '40px' }}>
      {/* spacer div approach */}
      <SpacerDivList data={data} />

      {/* absolute positioning approach */}
      <AbsolutePositionList data={data} />

      {/* PaddingTop approach */}
      <PaddingTop data={data} />
    </div>
  )
}



