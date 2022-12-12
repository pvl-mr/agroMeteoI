import React, { Suspense } from 'react'
import { Routes, Route, Outlet, Navigate, generatePath } from 'react-router-dom'
import { createBrowserHistory } from 'history'

import Layout from 'components/Layout'
import paths from 'сonstants/paths'

import { GlobalStyle } from './globalStyles'
import { DEFAULT_STATION } from 'сonstants/stations'

const Main = React.lazy(() => import('pages/Main'))

export const history = createBrowserHistory()

const RenderLayout: React.FC = () => {
  return (
    <Layout>
      <Outlet />
    </Layout>
  )
}

const App: React.FC = (): JSX.Element => {
  const defaultURL = generatePath(paths.MAIN, {
    stationId: DEFAULT_STATION,
  })
  return (
    <>
      <GlobalStyle />
      <Suspense fallback={<div>Загрузка...</div>}>
        <Routes>
          <Route path={paths.MAIN} element={<RenderLayout />}>
            <Route index element={<Main />} />
          </Route>
          <Route path="*" element={<Navigate to={defaultURL} replace />} />
        </Routes>
      </Suspense>
    </>
  )
}

export default App
