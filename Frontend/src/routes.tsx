import { createBrowserRouter, Link } from 'react-router-dom'
import { VirtualList, TabList } from './components'

const data = Array.from({ length: 10000 }, (_, i) => ({ id: i, name: `Item ${i}` }))

const tabs = Array.from({length:5}, (_,i)=> ({id:i, name: `Tab ${i}`}));

function Layout() {
  return (
    <>
      <h3>Machine Coding</h3>
      <nav>
        <div>
          <Link to="/virtual-list">Virtual List</Link>
        </div>
        <div>
          <Link to="/tab">Tab</Link>
        </div>
      </nav>
    </>
  )
}

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
  },
  {
    path: '/virtual-list',
    element: <VirtualList data={data} />,
  },
    {
    path: '/tab',
    element: <TabList tabs={tabs}/>,
  },
])
