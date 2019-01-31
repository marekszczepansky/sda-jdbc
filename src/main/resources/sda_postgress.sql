--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.4
-- Dumped by pg_dump version 9.2.4
-- Started on 2017-02-02 12:01:07

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 6 (class 2615 OID 90115)
-- Name: sda; Type: SCHEMA; Schema: -; Owner: postgres
--
DROP SCHEMA IF EXISTS sda CASCADE;

CREATE SCHEMA sda;


ALTER SCHEMA sda OWNER TO postgres;

SET search_path = sda, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 90161)
-- Name: dept; Type: TABLE; Schema: sda; Owner: postgres; Tablespace:
--

CREATE TABLE dept (
    deptno integer NOT NULL,
    dname text NOT NULL,
    location text NOT NULL
);


ALTER TABLE sda.dept OWNER TO postgres;


--
-- TOC entry 174 (class 1259 OID 90170)
-- Name: emp; Type: TABLE; Schema: sda; Owner: postgres; Tablespace:
--

CREATE TABLE emp (
    empno integer NOT NULL,
    ename text NOT NULL,
    job text NOT NULL,
    manager integer,
    hiredate date,
    salary numeric,
    commision numeric,
    deptno integer NOT NULL
);


ALTER TABLE sda.emp OWNER TO postgres;


--
-- TOC entry 1939 (class 0 OID 90161)
-- Dependencies: 172
-- Data for Name: dept; Type: TABLE DATA; Schema: sda; Owner: postgres
--

INSERT INTO dept VALUES (10, 'Accounting', 'New York');
INSERT INTO dept VALUES (20, 'Research', 'Dallas');
INSERT INTO dept VALUES (30, 'Sales', 'Chicago');
INSERT INTO dept VALUES (40, 'Operations', 'Boston');


--
-- TOC entry 1941 (class 0 OID 90170)
-- Dependencies: 174
-- Data for Name: emp; Type: TABLE DATA; Schema: sda; Owner: postgres
--

INSERT INTO emp VALUES (7369, 'SMITH', 'CLERK', 7902, '1993-06-13', 800, 0.00, 20);
INSERT INTO emp VALUES (7499, 'ALLEN', 'SALESMAN', 7698, '1998-08-15', 1600, 300, 30);
INSERT INTO emp VALUES (7521, 'WARD', 'SALESMAN', 7698, '1996-03-26', 1250, 500, 30);
INSERT INTO emp VALUES (7566, 'JONES', 'MANAGER', 7839, '1995-10-31', 2975, NULL, 20);
INSERT INTO emp VALUES (7698, 'BLAKE', 'MANAGER', 7839, '1992-06-11', 2850, NULL, 30);
INSERT INTO emp VALUES (7782, 'CLARK', 'MANAGER', 7839, '1993-05-14', 2450, NULL, 10);
INSERT INTO emp VALUES (7788, 'SCOTT', 'ANALYST', 7566, '1996-03-05', 3000, NULL, 20);
INSERT INTO emp VALUES (7839, 'KING', 'PRESIDENT', NULL, '1990-06-09', 5000, 0, 10);
INSERT INTO emp VALUES (7844, 'TURNER', 'SALESMAN', 7698, '1995-06-04', 1500, 0, 30);
INSERT INTO emp VALUES (7876, 'ADAMS', 'CLERK', 7788, '1999-06-04', 1100, NULL, 20);
INSERT INTO emp VALUES (7900, 'JAMES', 'CLERK', 7698, '2000-06-23', 950, NULL, 30);
INSERT INTO emp VALUES (7934, 'MILLER', 'CLERK', 7782, '2000-01-21', 1300, NULL, 10);
INSERT INTO emp VALUES (7902, 'FORD', 'ANALYST', 7566, '1997-12-05', 3000, NULL, 20);
INSERT INTO emp VALUES (7654, 'MARTIN', 'SALESMAN', 7698, '1998-12-05', 1250, 1400, 30);

--
-- TOC entry 1934 (class 2606 OID 90178)
-- Name: dept_pkey; Type: CONSTRAINT; Schema: sda; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY dept
    ADD CONSTRAINT dept_pkey PRIMARY KEY (deptno);


--
-- TOC entry 1936 (class 2606 OID 90180)
-- Name: emp_pkey; Type: CONSTRAINT; Schema: sda; Owner: postgres; Tablespace:
--

ALTER TABLE ONLY emp
    ADD CONSTRAINT emp_pkey PRIMARY KEY (empno);


--
-- TOC entry 1937 (class 2606 OID 90181)
-- Name: emp_dept_fk; Type: FK CONSTRAINT; Schema: sda; Owner: postgres
--

ALTER TABLE ONLY emp
    ADD CONSTRAINT emp_dept_fk FOREIGN KEY (deptno) REFERENCES dept(deptno);


-- Completed on 2017-02-02 12:01:07

--
-- PostgreSQL database dump complete
--

CREATE OR REPLACE FUNCTION sda.calculate_salary_by_dept(v_deptno integer)
  RETURNS numeric AS
$BODY$
    DECLARE
       cur_deps  CURSOR FOR
	 Select sum(e.salary)
	 from sda.dept d, sda.emp e
	WHERE d.deptno = e.deptno
	 AND d.deptno = v_deptno;
	v_salary NUMERIC;
    BEGIN
      OPEN cur_deps;
      FETCH cur_deps INTO v_salary;
      CLOSE cur_deps;

      return v_salary;
    END;
    $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sda.calculate_salary_by_dept(integer)
  OWNER TO postgres;
