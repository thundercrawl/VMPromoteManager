package org.zst.RestService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.zst.dbs.InstantiateDB;
import org.zst.dbs.objects.zstUser;
import org.zst.engine.Config;
import org.zst.engine.WorkerManager;

@SpringBootApplication
public class Application
{
   static Log log = LogFactory.getLog(Application.class);

   public static void main(String[] args)
   {

      Config.getInstance().startUp();
      InstantiateDB.getInstance().startUp();
      WorkerManager.getInstance().startUp();

      SpringApplication.run(Application.class, args);
   }

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter
{
   private Log log = LogFactory.getLog(WebSecurityConfiguration.class);

   @Override
   public void init(AuthenticationManagerBuilder auth) throws Exception
   {
      auth.userDetailsService(userDetailsService());
   }

   @Bean
   UserDetailsService userDetailsService()
   {
      return new UserDetailsService()
      {

         @Override
         public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
         {
            log.info("load user=" + username);
            zstUser account = zstUser.findByUsername(username);
            if (account != null)
            {
               return new User(account.getUserName(), account.getPassword(), true, true, true, true,
                     AuthorityUtils.createAuthorityList("USER"));
            }
            else
            {
               throw new UsernameNotFoundException("could not find the user '" + username + "'");
            }
         }

      };
   }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

   @Override
   protected void configure(HttpSecurity http) throws Exception
   {
      //  http.formLogin().loginPage("/login.html").permitAll();
      http.authorizeRequests().anyRequest().fullyAuthenticated().and().httpBasic().and().formLogin().loginPage(
            "/login.html").permitAll().and().csrf().disable();
   }

}
