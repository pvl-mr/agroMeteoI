import styled, { createGlobalStyle } from 'styled-components'

export const GlobalStyle = createGlobalStyle`
  body {
    background: #edeef0;
    margin: 0;
  }
`

export const MainLayout = styled.main`
  width: 100%;
  display: flex;
  justify-content: center;
  margin-top: 100px;
`

export const Content = styled.div`
  max-width: 800px;
  width: 800px;
`
